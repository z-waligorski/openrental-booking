package com.eprogram.openrental_booking.dto;

import com.eprogram.openrental_booking.validation.ValidDateRange;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@ValidDateRange
public record BookingDTO(UUID id,
                         @NotNull
                         UUID vehicleId,
                         @NotNull
                         UUID customerId,
                         @NotNull
                         @FutureOrPresent
                         LocalDate startDate,
                         @NotNull
                         @Future
                         LocalDate endDate) implements HavingDateRange {
}
