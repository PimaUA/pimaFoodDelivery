package com.pimaua.core.entity.enums;

import lombok.Getter;

@Getter
public enum DayOfWeek {
    SUNDAY(0), MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6);

    private final int value;

    DayOfWeek(int value) {
        this.value = value;
    }
}
