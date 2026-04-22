package com.eprogram.openrental_booking.exception;

import java.util.List;

public record ErrorResponse(List<ValidationError> errors) {
}
