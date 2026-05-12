package com.eprogram.openrental_booking.exception.api;

public record ValidationError(String field, String message) {
}
