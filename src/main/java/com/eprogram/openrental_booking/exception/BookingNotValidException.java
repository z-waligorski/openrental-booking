package com.eprogram.openrental_booking.exception;

public class BookingNotValidException extends RuntimeException {
    public BookingNotValidException() {
        super("Booking data is incorrect, check vehicle id, start and end dates");
    }
}
