package com.eprogram.openrental_booking.exception.integration;

public class VehiclesServiceInvalidRequestException extends RuntimeException {
    public VehiclesServiceInvalidRequestException() {
        super("Request to vehicle service is invalid");
    }
}
