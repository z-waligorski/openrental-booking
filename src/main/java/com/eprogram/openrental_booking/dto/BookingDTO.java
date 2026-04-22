package com.eprogram.openrental_booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

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
                         LocalDate endDate) {
}
