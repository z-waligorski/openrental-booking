package com.eprogram.openrental_booking.validation;

import com.eprogram.openrental_booking.dto.HavingDateRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DataRangeValidator implements ConstraintValidator<ValidDateRange, HavingDateRange> {

    @Override
    public boolean isValid(HavingDateRange value, ConstraintValidatorContext context) {
        if(value == null || value.startDate() == null || value.endDate() == null) {
            return true;
        }

        return value.startDate().isBefore(value.endDate());
    }
}
