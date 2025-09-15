package com.pimaua.core.validation;

import com.pimaua.core.dto.restaurant.OpeningHoursRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;

public class OpeningHoursValidator implements ConstraintValidator<ValidOpeningHours, OpeningHoursRequestDto> {

    @Override
    public boolean isValid(OpeningHoursRequestDto dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }
        boolean is24h = dto.getIs24Hours();
        LocalTime opensAt = dto.getOpensAt();
        LocalTime closesAt = dto.getClosesAt();
        boolean opensSet = opensAt != null;
        boolean closesSet = closesAt != null;

        if (is24h && (opensSet || closesSet)) {
            return buildViolation(context, "opensAt and closesAt must be null when is24Hours is true");
        }
        if (!is24h && (!opensSet || !closesSet)) {
            return buildViolation(context, "opensAt and closesAt must be provided when is24Hours is false");
        }
        if (!is24h && !closesAt.isAfter(opensAt)) {
            return buildViolation(context, "closesAt must be strictly after opensAt for non-24h schedule");
        }
        return true;
    }

    private boolean buildViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        return false;
    }
}
