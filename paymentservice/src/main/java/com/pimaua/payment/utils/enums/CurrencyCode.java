package com.pimaua.payment.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CurrencyCode {
    USD("usd"),
    EUR("eur"),
    UAH("uah"),
    CNY("cny"),
    JPY("jpy"),
    TRY("try"),
    PLN("pln"),
    MDL("mdl"),
    RON("ron");

    private final String code;
}
