package com.eprogram.openrental_booking.exception.integration;

public class VehiclesServiceUnavailableException extends RuntimeException {
    public VehiclesServiceUnavailableException() {
        super("Vehicles service is temporarily unavailable");
    }
}
