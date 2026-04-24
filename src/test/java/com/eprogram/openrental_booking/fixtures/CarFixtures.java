package com.eprogram.openrental_booking.fixtures;

import com.eprogram.openrental_booking.dto.CarDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CarFixtures {

    public static final String ID_STRING_CAR_TOYOTA = "11111111-1111-1111-1111-111111111111";
    public static final String ID_STRING_CAR_HONDA = "22222222-2222-2222-2222-222222222222";
    public static final String ID_STRING_CAR_FIAT = "33333333-3333-3333-3333-333333333333";

    public static final String ID_STRING_CUSTOMER = "cccccccc-cccc-cccc-cccc-cccccccccccc";
    public static final String ID_STRING_BOOKING = "bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb";

    public static final UUID ID_CAR_TOYOTA = UUID.fromString(ID_STRING_CAR_TOYOTA);
    public static final UUID ID_CAR_HONDA = UUID.fromString(ID_STRING_CAR_HONDA);
    public static final UUID ID_CAR_FIAT = UUID.fromString(ID_STRING_CAR_FIAT);

    public static final UUID ID_CUSTOMER = UUID.fromString(ID_STRING_CUSTOMER);
    public static final UUID ID_BOOKING = UUID.fromString(ID_STRING_BOOKING);

    public static CarDTO getToyota() {
        return new CarDTO(ID_CAR_TOYOTA, "Toyota", "Corolla", 2020, 5);
    }

    public static CarDTO getHonda() {
        return new CarDTO(ID_CAR_HONDA, "Honda", "Civic", 2023, 5);
    }

    public static CarDTO getFiat() {
        return new CarDTO(ID_CAR_FIAT, "Fiat", "500", 2025, 4);
    }

    public static List<CarDTO> getListOfCars() {
        return new ArrayList<>(List.of(getToyota(), getHonda(), getFiat()));
    }
}
