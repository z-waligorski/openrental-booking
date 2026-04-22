package com.eprogram.openrental_booking.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CarDTO(UUID id,
                     String brand,
                     String model,
                     @Min(1885)
                     Integer yearOfProduction,
                     @Positive
                     Integer seats
) {}
