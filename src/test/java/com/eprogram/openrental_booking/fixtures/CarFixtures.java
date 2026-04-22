package com.eprogram.openrental_booking.fixtures;

import com.eprogram.openrental_booking.dto.CarDTO;

import java.util.UUID;

public class CarFixtures {
    public static final String ID_STRING = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa";

    public static CarDTO getCarDTO() {
        return new CarDTO(UUID.fromString(ID_STRING),
                "Toyota", "Corolla", 2020, 5);
    }
}
