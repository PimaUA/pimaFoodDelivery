package com.pimaua.core.dto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EntityType {
    CUSTOMER("Customer"),
    RESTAURANT("Restaurant"),
    ORDER("Order"),
    ORDERITEM("OrderItem"),
    MENU("Menu"),
    MENUITEM("MenuItem"),
    OPENINGHOURS("OpeningHours");

    private final String displayName;
}
