package com.pimaua.payment.dto;

import com.pimaua.payment.utils.enums.PaymentMethodType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreateDto {
    @NotNull(message = "Amount must be provided")
    private BigDecimal amount;
    @NotBlank(message = "Currency type must be provided")
    private String currency;
    private PaymentMethodType paymentMethodType;
    @NotBlank(message = "Payment method ID must be provided for confirmation")
    private String paymentMethodId;
}
