package com.eprogram.openrental_booking.fixtures;

import com.eprogram.openrental_booking.dto.CarAvailabilityRequestDTO;

import java.time.LocalDate;

public class CarAvailabilityFixtures {

    public static CarAvailabilityRequestDTO getCarAvailabilityDTOWithCorrectDatesOnly() {
        return new CarAvailabilityRequestDTO(null,
                null,
                null,
                null,
                LocalDate.of(2126, 1, 3),
                LocalDate.of(2126, 1, 7));
    }

    public static CarAvailabilityRequestDTO getCarAvailabilityDTO() {
        return new CarAvailabilityRequestDTO("Toyota",
                "Corolla",
                2020,
                5,
                LocalDate.of(2126, 1, 3),
                LocalDate.of(2126, 1, 7));
    }
}
