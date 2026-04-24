package com.eprogram.openrental_booking.exception;

import java.util.UUID;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(UUID id) {
        super("Booking with id " + id + " not found");
    }
}
