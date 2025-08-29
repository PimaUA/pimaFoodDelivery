package com.pimaua.payment.service;

import com.pimaua.payment.client.OrderServiceClient;
import com.pimaua.payment.dto.OrderForPaymentDto;
import com.pimaua.payment.dto.PaymentCreateDto;
import com.pimaua.payment.dto.ResponseDto;
import com.pimaua.payment.entity.Payment;
import com.pimaua.payment.exception.custom.PaymentProcessingException;
import com.pimaua.payment.mapper.StripePaymentStatusMapper;
import com.pimaua.payment.repository.PaymentRepository;
import com.pimaua.payment.service.impl.PaymentServiceImpl;
import com.pimaua.payment.utils.enums.PaymentMethodType;
import com.pimaua.payment.utils.enums.PaymentStatus;
import com.stripe.exception.ApiException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderServiceClient orderServiceClient;
    @Mock
    private StripePaymentStatusMapper statusMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private PaymentCreateDto requestDto;
    private OrderForPaymentDto orderDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDto = new PaymentCreateDto();
        requestDto.setAmount(BigDecimal.valueOf(50.00));
        requestDto.setCurrency("usd");
        requestDto.setPaymentMethodType(PaymentMethodType.CARD);

        orderDto = new OrderForPaymentDto();
        orderDto.setId(123);
        orderDto.setUserId(456);
    }

    @Test
    void shouldCreatePaymentIntentSuccessfully() throws Exception {
        // given
        ResponseDto<OrderForPaymentDto> responseDto =
                new ResponseDto<>("200", "success", orderDto);
        when(paymentRepository.existsByOrderId(123)).thenReturn(false);
        when(orderServiceClient.fetchOrderDetails(123)).thenReturn(responseDto);

        // Mock Stripe PaymentIntent
        PaymentIntent mockIntent = new PaymentIntent();
        mockIntent.setId("pi_12345");
        mockIntent.setStatus("succeeded");

        // Static mocking for PaymentIntent.create()
        try (var mocked = mockStatic(PaymentIntent.class)) {
            mocked.when(() ->
                    PaymentIntent.create(any(PaymentIntentCreateParams.class), any(RequestOptions.class))
            ).thenReturn(mockIntent);

            when(statusMapper.map("succeeded")).thenReturn(PaymentStatus.SUCCEEDED);

            // when
            PaymentIntent result = paymentService.createPaymentIntent(requestDto, 123);

            // then
            assertNotNull(result);
            assertEquals("pi_12345", result.getId());
            verify(paymentRepository).save(any(Payment.class));
            verify(statusMapper).map("succeeded");
        }
    }

    @Test
    void shouldThrowIfPaymentAlreadyExists() {
        // given
        when(paymentRepository.existsByOrderId(123)).thenReturn(true);

        // when / then
        assertThrows(PaymentProcessingException.class,
                () -> paymentService.createPaymentIntent(requestDto, 123));

        verify(paymentRepository, never()).save(any());
    }

    @Test
    void shouldThrowOnStripeException() throws Exception {
        // given
        ResponseDto<OrderForPaymentDto> responseDto =
                new ResponseDto<>("200", "success", orderDto);
        when(paymentRepository.existsByOrderId(123)).thenReturn(false);
        when(orderServiceClient.fetchOrderDetails(123)).thenReturn(responseDto);

        try (var mocked = mockStatic(PaymentIntent.class)) {
            mocked.when(() ->
                    PaymentIntent.create(any(PaymentIntentCreateParams.class), any(RequestOptions.class))
            ).thenThrow(new ApiException("fail", null, null, 400, null));

            // when / then
            assertThrows(PaymentProcessingException.class,
                    () -> paymentService.createPaymentIntent(requestDto, 123));
        }
    }
}
