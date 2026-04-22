package com.eprogram.openrental_booking.controller;

import com.eprogram.openrental_booking.dto.CarAvailabilityRequestDTO;
import com.eprogram.openrental_booking.dto.CarDTO;
import com.eprogram.openrental_booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}
