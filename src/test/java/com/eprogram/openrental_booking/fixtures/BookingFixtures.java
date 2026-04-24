package com.eprogram.openrental_booking.fixtures;

import com.eprogram.openrental_booking.dto.BookingDTO;
import com.eprogram.openrental_booking.model.Booking;

import java.time.LocalDate;
import java.util.UUID;

import static com.eprogram.openrental_booking.fixtures.CarFixtures.ID_CAR_TOYOTA;
import static com.eprogram.openrental_booking.fixtures.CarFixtures.ID_CUSTOMER;

public class BookingFixtures {
    public static final UUID GENERATED_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    public static Booking getInputBooking() {
        Booking inputBooking = new Booking();
        inputBooking.setCustomerId(ID_CUSTOMER);
        inputBooking.setVehicleId(ID_CAR_TOYOTA);
        inputBooking.setStartDate(LocalDate.of(2126, 3, 5));
        inputBooking.setEndDate(LocalDate.of(2126, 3, 7));
        return inputBooking;
    }

    public static Booking getOutputBooking() {
        Booking outputBooking = getInputBooking();
        outputBooking.setId(GENERATED_ID);
        return outputBooking;
    }

    public static BookingDTO getInputBookingDTO() {
        return new BookingDTO(null, ID_CAR_TOYOTA, ID_CUSTOMER,
                LocalDate.of(2126, 3, 5),
                LocalDate.of(2126, 3, 7));
    }

    public static BookingDTO getOutputBookingDTO() {
        return new BookingDTO(GENERATED_ID, ID_CAR_TOYOTA, ID_CUSTOMER,
                LocalDate.of(2126, 3, 5),
                LocalDate.of(2126, 3, 7));
    }
}
