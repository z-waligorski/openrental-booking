package com.eprogram.openrental_booking.controller;

import com.eprogram.openrental_booking.dto.BookingDTO;
import com.eprogram.openrental_booking.dto.CarAvailabilityRequestDTO;
import com.eprogram.openrental_booking.dto.CarDTO;
import com.eprogram.openrental_booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/booking")
public class CarBookingController {
    private final BookingService bookingService;

    @GetMapping("/api/v1/available-cars")
    public ResponseEntity<List<CarDTO>> getFilteredVehicles(@Valid @ModelAttribute CarAvailabilityRequestDTO dto) {
        List<CarDTO> matchingCars = bookingService.getCarsAvailableForFiltersAndDates(dto);
        return ResponseEntity.ok(matchingCars);
    }

    @PostMapping("/api/v1/book")
    public ResponseEntity<BookingDTO> saveBooking(@RequestBody @Valid BookingDTO bookingDTO) {
        BookingDTO savedBooking = bookingService.validateAndSaveBooking(bookingDTO);
        return ResponseEntity.created(URI.create("/booking/api/v1/bookings/" + savedBooking.id()))
                .body(savedBooking);
    }

    @GetMapping("/api/v1/bookings/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable UUID id) {
        BookingDTO bookingDTO = bookingService.getBookingById(id);
        return ResponseEntity.ok(bookingDTO);
    }
}
