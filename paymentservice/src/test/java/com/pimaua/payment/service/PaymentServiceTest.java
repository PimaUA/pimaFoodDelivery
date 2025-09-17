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
import com.pimaua.payment.service.testdata.PaymentServiceTestData;
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

        requestDto = PaymentServiceTestData.mockPaymentCreateDto();
        orderDto = PaymentServiceTestData.mockOrderForPaymentDto();
        payment = PaymentServiceTestData.mockPayment(requestDto, orderDto);
    }

    //createPaymentIntent tests
    @Test
    void shouldCreatePaymentIntentSuccessfully() throws Exception {
        //given: no existing payment for order and order service returns valid order
        ResponseDto<OrderForPaymentDto> responseDto =
                new ResponseDto<>("200", "success", orderDto);

        when(paymentRepository.existsByOrderId(123)).thenReturn(false);
        when(orderServiceClient.fetchOrderDetails(123)).thenReturn(responseDto);
        when(statusMapper.map("succeeded")).thenReturn(PaymentStatus.SUCCEEDED);

        // and: Stripe PaymentIntent mock configured to return a successful intent
        PaymentIntent mockIntent = new PaymentIntent();
        mockIntent.setId("pi_12345");
        mockIntent.setStatus("succeeded");

        try (var mocked = mockStatic(PaymentIntent.class)) {
            mocked.when(() ->
                    PaymentIntent.create(any(PaymentIntentCreateParams.class), any(RequestOptions.class))
            ).thenReturn(mockIntent);
            when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

            // when: creating a new payment intent
            PaymentIntent result = paymentService.createPaymentIntent(requestDto, 123);

            // then: result is returned, payment is persisted, and status is mapped
            assertNotNull(result);
            assertEquals("pi_12345", result.getId());
            verify(paymentRepository).save(any(Payment.class));
            verify(statusMapper).map("succeeded");
        }
    }

    @Test
    void shouldThrowIfPaymentAlreadyExists() {
        // given: a payment already exists for this order
        when(paymentRepository.existsByOrderId(123)).thenReturn(true);

        // when & then: creating a new payment should throw exception and not persist anything
        assertThrows(PaymentProcessingException.class,
                () -> paymentService.createPaymentIntent(requestDto, 123));

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void shouldThrowOnStripeException() throws Exception {
        // given: order exists and Stripe API will fail
        ResponseDto<OrderForPaymentDto> responseDto =
                new ResponseDto<>("200", "success", orderDto);

        when(paymentRepository.existsByOrderId(123)).thenReturn(false);
        when(orderServiceClient.fetchOrderDetails(123)).thenReturn(responseDto);

        try (var mocked = mockStatic(PaymentIntent.class)) {
            mocked.when(() ->
                    PaymentIntent.create(any(PaymentIntentCreateParams.class), any(RequestOptions.class))
            ).thenThrow(new ApiException("fail", null, null, 400, null));

            // when & then: creating a payment should wrap Stripe exception into PaymentProcessingException
            assertThrows(PaymentProcessingException.class,
                    () -> paymentService.createPaymentIntent(requestDto, 123));
        }
    }

    @Test
    void shouldThrowIfOrderServiceUnavailable() {
        // given: order service call fails due to service unavailability
        when(paymentRepository.existsByOrderId(123)).thenReturn(false);
        when(orderServiceClient.fetchOrderDetails(123))
                .thenThrow(new ServiceClientException("Service down"));

        // when & then: exception is propagated
        assertThrows(ServiceClientException.class,
                () -> paymentService.createPaymentIntent(requestDto, 123));
    }

    @Test
    void shouldThrowIfOrderNotFound() {
        // given: order service reports order not found
        when(paymentRepository.existsByOrderId(123)).thenReturn(false);
        when(orderServiceClient.fetchOrderDetails(123))
                .thenThrow(new OrderNotFoundException("Order missing"));

        // when & then: exception is propagated
        assertThrows(OrderNotFoundException.class,
                () -> paymentService.createPaymentIntent(requestDto, 123));
    }

    // updatePaymentStatusAndCreateOutbox tests
    @Test
    void shouldUpdatePaymentAndCreateOutboxSuccessfully() throws Exception {
        // given: existing payment found in repository
        Payment existingPayment = Payment.builder()
                .id(1)
                .paymentIntentId("pi_12345")
                .paymentStatus(PaymentStatus.SUCCEEDED)
                .orderId(123)
                .userId(456)
                .build();

        when(paymentRepository.findByPaymentIntentId("pi_12345")).thenReturn(Optional.of(existingPayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(existingPayment);

        // and: OutboxEventFactory mock produces an event
        OutboxEvent outboxEvent = OutboxEvent.builder().id(1).build();
        try (var mockedFactory = mockStatic(OutboxEventFactory.class)) {
            mockedFactory.when(() ->
                    OutboxEventFactory.createOrderOutboxEvent(anyInt(), any(), any(), any(ObjectMapper.class))
            ).thenReturn(outboxEvent);

            // when: updating payment status and generating outbox event
            Payment updatedPayment = paymentService.updatePaymentStatusAndCreateOutbox(
                    "pi_12345", PaymentStatus.SUCCEEDED, 123);

            // then: payment is updated and outbox event saved
            assertNotNull(updatedPayment);
            assertEquals(PaymentStatus.SUCCEEDED, updatedPayment.getPaymentStatus());
            verify(paymentRepository).save(existingPayment);
            verify(outboxEventRepository).save(outboxEvent);
        }
    }

    @Test
    void shouldThrowIfPaymentNotFoundOnUpdate() {
        // given: no payment found for provided intent ID
        when(paymentRepository.findByPaymentIntentId("pi_999")).thenReturn(Optional.empty());

        // when & then: update should fail with PaymentNotFoundException and no persistence occurs
        assertThrows(PaymentNotFoundException.class,
                () -> paymentService.updatePaymentStatusAndCreateOutbox("pi_999",
                        PaymentStatus.SUCCEEDED, 123));

        verify(paymentRepository, never()).save(any());
        verify(outboxEventRepository, never()).save(any());
    }
}
