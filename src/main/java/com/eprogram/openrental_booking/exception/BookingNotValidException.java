package com.eprogram.openrental_booking.exception;

public class BookingNotValidException extends RuntimeException {
    public BookingNotValidException() {
        super("Booking data is incorrect, check start and end dates");
    }
}
