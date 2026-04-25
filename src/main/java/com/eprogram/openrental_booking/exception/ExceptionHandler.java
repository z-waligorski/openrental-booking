package com.eprogram.openrental_booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationErrors(MethodArgumentNotValidException e) {
        List<ValidationError> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ValidationError(error.getField(), error.getDefaultMessage()))
                .toList();

        List<ValidationError> globalErrors = e.getBindingResult().getGlobalErrors().stream()
                .map(error -> new ValidationError(error.getObjectName(), error.getDefaultMessage()))
                .toList();

        List<ValidationError> allErrors = new ArrayList<>();
        allErrors.addAll(fieldErrors);
        allErrors.addAll(globalErrors);

        return new ErrorResponse(allErrors);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBookingNotValidException(BookingNotValidException e) {
        return e.getMessage();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBookingNotFoundException(BookingNotFoundException e) {
        return e.getMessage();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleVehicleNotFoundException(VehicleNotFoundException e) {
        return e.getMessage();
    }
}
