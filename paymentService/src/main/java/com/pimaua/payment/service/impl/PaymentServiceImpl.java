package com.pimaua.payment.service.impl;

import com.pimaua.payment.client.OrderServiceClient;
import com.pimaua.payment.dto.OrderForPaymentDto;
import com.pimaua.payment.dto.PaymentCreateDto;
import com.pimaua.payment.entity.Payment;
import com.pimaua.payment.exception.custom.OrderNotFoundException;
import com.pimaua.payment.exception.custom.PaymentNotFoundException;
import com.pimaua.payment.exception.custom.PaymentProcessingException;
import com.pimaua.payment.exception.custom.ServiceClientException;
import com.pimaua.payment.mapper.StripePaymentStatusMapper;
import com.pimaua.payment.repository.PaymentRepository;
import com.pimaua.payment.service.PaymentService;
import com.pimaua.payment.utils.StripeAmountConverter;
import com.pimaua.payment.utils.enums.PaymentStatus;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderServiceClient orderServiceClient;
    private final StripePaymentStatusMapper stripePaymentStatusMapper;
    private final static Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    public PaymentIntent createPaymentIntent(PaymentCreateDto paymentCreateDto, Integer orderId) {
        // Check if payment already exists for this order
        if (paymentRepository.existsByOrderId(orderId)) {
            logger.warn("Payment already exists for orderId={}", orderId);
            throw new PaymentProcessingException("Payment already exists for this order");
        }
        try {
            // Fetch order details first
            OrderForPaymentDto orderForPaymentDto = orderServiceClient.fetchOrderDetails(orderId).getData();
            // Convert amount to Stripe format
            Long stripeAmount = StripeAmountConverter.convertToStripeAmount(
                    paymentCreateDto.getAmount(),
                    paymentCreateDto.getCurrency()
            );
            // Create Stripe PaymentIntent
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(stripeAmount)
                    .setCurrency(paymentCreateDto.getCurrency())
                    .addPaymentMethodType(paymentCreateDto.getPaymentMethodType().getCode())
                    .setPaymentMethod(paymentCreateDto.getPaymentMethodId())
                    .setConfirm(true) // Confirm immediately
                    .putMetadata("order_id", String.valueOf(orderForPaymentDto.getId()))
                    .putMetadata("user_id", String.valueOf(orderForPaymentDto.getUserId()))
                    .setSetupFutureUsage(PaymentIntentCreateParams.SetupFutureUsage.ON_SESSION)
                    .build();
            // Use idempotency key to prevent accidental duplicates on retries
            RequestOptions options = RequestOptions.builder()
                    .setIdempotencyKey("payment_" + orderId)
                    .build();
            PaymentIntent paymentIntent = PaymentIntent.create(params, options);
            // Save payment in DB
            savePaymentEntity(paymentCreateDto, orderId, paymentIntent, orderForPaymentDto.getUserId());
            logger.info("Payment intent created successfully for orderId={}, paymentIntentId={}",
                    orderId, paymentIntent.getId());
            return paymentIntent;
        } catch (ServiceClientException serviceClientException) {
            logger.error("OrderService is unavailable for orderId={}", orderId, serviceClientException);
            throw new ServiceClientException("OrderService is unavailable");
        } catch (OrderNotFoundException orderNotFoundException) {
            logger.warn("Order not found with orderId={}", orderId, orderNotFoundException);
            throw new OrderNotFoundException("Order not found with id: " + orderId);
        } catch (StripeException stripeException) {
            logger.error("Stripe payment failed for orderId={}", orderId, stripeException);
            throw new PaymentProcessingException("Failed to process payment with Stripe: " + stripeException.getMessage());
        } catch (Exception exception) {
            logger.error("Unexpected error during payment creation for orderId={}", orderId, exception);
            throw new PaymentProcessingException(
                    String.format("Payment processing failed (%s): %s",
                            exception.getClass().getSimpleName(),
                            exception.getMessage())
            );
        }
    }

    @Transactional
    public Payment updatePaymentStatus(String paymentIntentId, PaymentStatus status) {
        Payment payment = paymentRepository.findByPaymentIntentId(paymentIntentId)
                .orElseThrow(() -> {
                    logger.error("Payment not found with paymentIntentId={}", paymentIntentId);
                    return new PaymentNotFoundException("Order not found with paymentIntentId " + paymentIntentId);
                });
        payment.setPaymentStatus(status);
        return paymentRepository.save(payment);
    }

    @Transactional
    private Payment savePaymentEntity(PaymentCreateDto paymentCreateDto, Integer orderId, PaymentIntent intent,
                                      Integer userId) {
        Payment payment = Payment.builder()
                .orderId(orderId)
                .userId(userId)
                .amount(paymentCreateDto.getAmount())
                .currency(paymentCreateDto.getCurrency())
                .paymentStatus(stripePaymentStatusMapper.map(intent.getStatus()))
                .paymentIntentId(intent.getId())
                .build();
        return paymentRepository.save(payment);
    }
}
