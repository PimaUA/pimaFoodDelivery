package com.pimaua.payment.utils;

import java.math.BigDecimal;
import java.util.Currency;

public class StripeAmountConverter {

    public static Long convertToStripeAmount(BigDecimal amount, String currency) {
        if (amount == null) {
            return null;
        }
        Currency currencyCode = Currency.getInstance(currency.toUpperCase());
        int decimalPlaces = currencyCode.getDefaultFractionDigits();

        BigDecimal multiplier = BigDecimal.valueOf(10).pow(decimalPlaces);
        return amount.multiply(multiplier).longValue();
    }
}
