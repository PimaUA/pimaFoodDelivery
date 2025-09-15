package com.pimaua.payment.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PaymentMethodType {
    CARD("card"),
    US_BANK_ACCOUNT("us_bank_account"),
    SEPA_DEBIT("sepa_debit"),
    WECHAT_PAY("wechat_pay"),
    APPLE_PAY("apple_pay"),
    REVOLUT_PAY("revolut_pay"),
    PAYPAL("paypal"),
    GOOGLE_PAY("google_pay");

    private final String code;
}
