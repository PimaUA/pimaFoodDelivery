package com.pimaua.payment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pimaua.payment.client.OrderServiceClient;
import com.pimaua.payment.dto.OrderForPaymentDto;
import com.pimaua.payment.dto.PaymentCreateDto;
import com.pimaua.payment.dto.ResponseDto;
import com.pimaua.payment.entity.OutboxEvent;
import com.pimaua.payment.entity.Payment;
import com.pimaua.payment.exception.custom.OrderNotFoundException;
import com.pimaua.payment.exception.custom.PaymentNotFoundException;
import com.pimaua.payment.exception.custom.PaymentProcessingException;
import com.pimaua.payment.exception.custom.ServiceClientException;
import com.pimaua.payment.mapper.StripePaymentStatusMapper;
import com.pimaua.payment.repository.OutboxEventRepository;
import com.pimaua.payment.repository.PaymentRepository;
import com.pimaua.payment.service.events.OutboxEventFactory;
import com.pimaua.payment.service.impl.PaymentServiceImpl;
import com.pimaua.payment.utils.enums.PaymentMethodType;
import com.pimaua.payment.utils.enums.PaymentStatus;
import com.stripe.exception.ApiException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Tag("unit")
class PaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private OutboxEventRepository outboxEventRepository;

    @Mock
    private OrderServiceClient orderServiceClient;

    @Mock
    private StripePaymentStatusMapper statusMapper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private PaymentCreateDto requestDto;
    private OrderForPaymentDto orderDto;
    private Payment payment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDto = new PaymentCreateDto();
        requestDto.setAmount(BigDecimal.valueOf(50.00));
        requestDto.setCurrency("usd");
        requestDto.setPaymentMethodType(PaymentMethodType.CARD);
        requestDto.setPaymentMethodId("pm_12345");

        orderDto = new OrderForPaymentDto();
        orderDto.setId(123);
        orderDto.setUserId(456);

        payment = Payment.builder()
                .id(1)
                .orderId(orderDto.getId())
                .userId(orderDto.getUserId())
                .amount(requestDto.getAmount())
                .currency(requestDto.getCurrency())
                .paymentStatus(PaymentStatus.SUCCEEDED)
                .paymentIntentId("pi_12345")
                .build();
    }

    //createPaymentIntent tests
    @Test
    void shouldCreatePaymentIntentSuccessfully() throws Exception {
        //given
        ResponseDto<OrderForPaymentDto> responseDto =
                new ResponseDto<>("200", "success", orderDto);

        when(paymentRepository.existsByOrderId(123)).thenReturn(false);
        when(orderServiceClient.fetchOrderDetails(123)).thenReturn(responseDto);
        when(statusMapper.map("succeeded")).thenReturn(PaymentStatus.SUCCEEDED);

        PaymentIntent mockIntent = new PaymentIntent();
        mockIntent.setId("pi_12345");
        mockIntent.setStatus("succeeded");

        try (var mocked = mockStatic(PaymentIntent.class)) {
            mocked.when(() ->
                    PaymentIntent.create(any(PaymentIntentCreateParams.class), any(RequestOptions.class))
            ).thenReturn(mockIntent);

            when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

            PaymentIntent result = paymentService.createPaymentIntent(requestDto, 123);

            assertNotNull(result);
            assertEquals("pi_12345", result.getId());
            verify(paymentRepository).save(any(Payment.class));
            verify(statusMapper).map("succeeded");
        }
    }

    @Test
    void shouldThrowIfPaymentAlreadyExists() {
        when(paymentRepository.existsByOrderId(123)).thenReturn(true);

        assertThrows(PaymentProcessingException.class,
                () -> paymentService.createPaymentIntent(requestDto, 123));

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void shouldThrowOnStripeException() throws Exception {
        ResponseDto<OrderForPaymentDto> responseDto =
                new ResponseDto<>("200", "success", orderDto);

        when(paymentRepository.existsByOrderId(123)).thenReturn(false);
        when(orderServiceClient.fetchOrderDetails(123)).thenReturn(responseDto);

        try (var mocked = mockStatic(PaymentIntent.class)) {
            mocked.when(() ->
                    PaymentIntent.create(any(PaymentIntentCreateParams.class), any(RequestOptions.class))
            ).thenThrow(new ApiException("fail", null, null, 400, null));

            assertThrows(PaymentProcessingException.class,
                    () -> paymentService.createPaymentIntent(requestDto, 123));
        }
    }

    @Test
    void shouldThrowIfOrderServiceUnavailable() {
        when(paymentRepository.existsByOrderId(123)).thenReturn(false);
        when(orderServiceClient.fetchOrderDetails(123))
                .thenThrow(new ServiceClientException("Service down"));

        assertThrows(ServiceClientException.class,
                () -> paymentService.createPaymentIntent(requestDto, 123));
    }

    @Test
    void shouldThrowIfOrderNotFound() {
        when(paymentRepository.existsByOrderId(123)).thenReturn(false);
        when(orderServiceClient.fetchOrderDetails(123))
                .thenThrow(new OrderNotFoundException("Order missing"));

        assertThrows(OrderNotFoundException.class,
                () -> paymentService.createPaymentIntent(requestDto, 123));
    }

    // updatePaymentStatusAndCreateOutbox tests
    @Test
    void shouldUpdatePaymentAndCreateOutboxSuccessfully() throws Exception {
        Payment existingPayment = Payment.builder()
                .id(1)
                .paymentIntentId("pi_12345")
                .paymentStatus(PaymentStatus.SUCCEEDED)
                .orderId(123)
                .userId(456)
                .build();

        when(paymentRepository.findByPaymentIntentId("pi_12345")).thenReturn(Optional.of(existingPayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(existingPayment);

        OutboxEvent outboxEvent = OutboxEvent.builder().id(1).build();
        try (var mockedFactory = mockStatic(OutboxEventFactory.class)) {
            mockedFactory.when(() ->
                    OutboxEventFactory.createOrderOutboxEvent(anyInt(), any(), any(), any(ObjectMapper.class))
            ).thenReturn(outboxEvent);

            Payment updatedPayment = paymentService.updatePaymentStatusAndCreateOutbox(
                    "pi_12345", PaymentStatus.SUCCEEDED, 123);

            assertNotNull(updatedPayment);
            assertEquals(PaymentStatus.SUCCEEDED, updatedPayment.getPaymentStatus());
            verify(paymentRepository).save(existingPayment);
            verify(outboxEventRepository).save(outboxEvent);
        }
    }

    @Test
    void shouldThrowIfPaymentNotFoundOnUpdate() {
        when(paymentRepository.findByPaymentIntentId("pi_999")).thenReturn(Optional.empty());

        assertThrows(PaymentNotFoundException.class,
                () -> paymentService.updatePaymentStatusAndCreateOutbox("pi_999",
                        PaymentStatus.SUCCEEDED, 123));

        verify(paymentRepository, never()).save(any());
        verify(outboxEventRepository, never()).save(any());
    }
}
