package com.eprogram.openrental_booking.exception.api;

import java.util.List;

public record ErrorResponse(List<ValidationError> errors) {
}
