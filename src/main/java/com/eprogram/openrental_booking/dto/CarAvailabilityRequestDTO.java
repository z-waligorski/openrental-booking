package com.eprogram.openrental_booking.dto;

import com.eprogram.openrental_booking.validation.ValidDateRange;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@ValidDateRange
public record CarAvailabilityRequestDTO(String brand,
                                        String model,
                                        Integer minYearOfProduction,
                                        Integer seats,
                                        @NotNull
                                        @FutureOrPresent
                                        LocalDate startDate,
                                        @NotNull
                                        @Future
                                        LocalDate endDate) {
}
