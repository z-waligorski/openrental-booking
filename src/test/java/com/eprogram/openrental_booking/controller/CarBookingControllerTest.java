package com.eprogram.openrental_booking.controller;

import com.eprogram.openrental_booking.dto.CarAvailabilityRequestDTO;
import com.eprogram.openrental_booking.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static com.eprogram.openrental_booking.fixtures.CarAvailabilityFixtures.getCarAvailabilityDTOWithCorrectDatesOnly;
import static com.eprogram.openrental_booking.fixtures.CarFixtures.ID_STRING;
import static com.eprogram.openrental_booking.fixtures.CarFixtures.getCarDTO;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarBookingController.class)
class CarBookingControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BookingService bookingService;

    @Test
    void getFilteredVehicles_shouldReturnListOfCars_whenCarsAvailable() throws Exception {
        when(bookingService.getCarsAvailableForFiltersAndDates(getCarAvailabilityDTOWithCorrectDatesOnly()))
                .thenReturn(List.of(getCarDTO()));

        mockMvc.perform(get("/booking/api/v1/available-cars?startDate=2126-01-03&endDate=2126-01-07"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(ID_STRING))
                .andExpect(jsonPath("$[0].brand").value("Toyota"))
                .andExpect(jsonPath("$[0].model").value("Corolla"))
                .andExpect(jsonPath("$[0].yearOfProduction").value(2020))
                .andExpect(jsonPath("$[0].seats").value(5));

        verify(bookingService).getCarsAvailableForFiltersAndDates(getCarAvailabilityDTOWithCorrectDatesOnly());
    }

    @Test
    void getFilteredVehicles_shouldReturnEmptyList_whenNoCarsAvailable() throws Exception {
        when(bookingService.getCarsAvailableForFiltersAndDates(any(CarAvailabilityRequestDTO.class)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/booking/api/v1/available-cars?startDate=2126-01-03&endDate=2126-01-07"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getFilteredVehicles_shouldReturnBadRequest_whenDatesAreMissing() throws Exception {
        mockMvc.perform(get("/booking/api/v1/available-cars"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("startDate", "endDate")))
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder("must not be null", "must not be null")));
    }

    @Test
    void getFilteredVehicles_shouldReturnBadRequest_whenDatesAreFromPast() throws Exception {
        mockMvc.perform(get("/booking/api/v1/available-cars?startDate=2020-01-03&endDate=2020-01-07"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("startDate", "endDate")))
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder("must be a date in the present or in the future", "must be a future date")));
    }

    @Test
    void getFilteredVehicles_shouldReturnBadRequest_whenStartAfterEnd() throws Exception {
        mockMvc.perform(get("/booking/api/v1/available-cars?startDate=2120-01-03&endDate=2120-01-02"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[*].field").value("carAvailabilityRequestDTO"))
                .andExpect(jsonPath("$.errors[*].message").value("startDate must be before endDate"));
    }

}