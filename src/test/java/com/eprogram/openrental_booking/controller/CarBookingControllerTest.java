package com.eprogram.openrental_booking.controller;

import com.eprogram.openrental_booking.dto.BookingDTO;
import com.eprogram.openrental_booking.dto.CarAvailabilityRequestDTO;
import com.eprogram.openrental_booking.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.eprogram.openrental_booking.fixtures.CarAvailabilityFixtures.getCarAvailabilityDTOWithCorrectDatesOnly;
import static com.eprogram.openrental_booking.fixtures.CarFixtures.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarBookingController.class)
class CarBookingControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BookingService bookingService;

    @Autowired
    ObjectMapper objectMapper;

    private static final String URI_PREFIX = "/booking/api/v1";

    @Test
    void getFilteredVehicles_shouldReturnListOfCars_whenCarsAvailable() throws Exception {
        when(bookingService.getCarsAvailableForFiltersAndDates(getCarAvailabilityDTOWithCorrectDatesOnly()))
                .thenReturn(List.of(getToyota()));

        mockMvc.perform(get(URI_PREFIX + "/available-cars?startDate=2126-01-03&endDate=2126-01-07"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(ID_STRING_CAR_TOYOTA))
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

        mockMvc.perform(get(URI_PREFIX + "/available-cars?startDate=2126-01-03&endDate=2126-01-07"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getFilteredVehicles_shouldReturnBadRequest_whenDatesAreMissing() throws Exception {
        mockMvc.perform(get(URI_PREFIX + "/available-cars"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("startDate", "endDate")))
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder("must not be null", "must not be null")));
    }

    @Test
    void getFilteredVehicles_shouldReturnBadRequest_whenDatesAreFromPast() throws Exception {
        mockMvc.perform(get(URI_PREFIX + "/available-cars?startDate=2020-01-03&endDate=2020-01-07"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("startDate", "endDate")))
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder("must be a date in the present or in the future", "must be a future date")));
    }

    @Test
    void getFilteredVehicles_shouldReturnBadRequest_whenStartAfterEnd() throws Exception {
        mockMvc.perform(get(URI_PREFIX + "/available-cars?startDate=2120-01-03&endDate=2120-01-02"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[*].field").value("carAvailabilityRequestDTO"))
                .andExpect(jsonPath("$.errors[*].message").value("startDate must be before endDate"));
    }

    @Test
    void saveBooking_returnsSavedBooking_whenInputCorrect() throws Exception {
        BookingDTO requestDTO = new BookingDTO(null, ID_CAR_TOYOTA, ID_CUSTOMER,
                LocalDate.of(2126, 4, 23),
                LocalDate.of(2126, 4, 25));
        BookingDTO response = new BookingDTO(UUID.randomUUID(), ID_CAR_TOYOTA, ID_CUSTOMER,
                LocalDate.of(2126, 4, 23),
                LocalDate.of(2126, 4, 25));

        when(bookingService.validateAndSaveBooking(requestDTO)).thenReturn(response);

        mockMvc.perform(post(URI_PREFIX + "/book")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/booking/api/v1/bookings/" + response.id()))
                .andExpect(jsonPath("$.id").value(response.id().toString()))
                .andExpect(jsonPath("$.vehicleId").value(ID_STRING_CAR_TOYOTA))
                .andExpect(jsonPath("$.customerId").value(ID_STRING_CUSTOMER))
                .andExpect(jsonPath("$.startDate").value("2126-04-23"))
                .andExpect(jsonPath("$.endDate").value("2126-04-25"));
    }

    @Test
    void saveBooking_shouldReturnBadRequest_whenInputIncorrect() throws Exception {
        BookingDTO requestDTO = new BookingDTO(null, null, null,
                LocalDate.of(1996, 4, 23),
                LocalDate.of(1996, 4, 22));

        mockMvc.perform(post(URI_PREFIX + "/book")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(5)))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("vehicleId", "customerId", "startDate", "endDate", "bookingDTO")))
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder("must not be null", "must not be null",
                        "startDate must be before endDate",
                        "must be a future date",
                        "must be a date in the present or in the future")));
    }


    @Test
    void saveBooking_shouldReturnBadRequest_whenDatesAreMissing() throws Exception {
        BookingDTO requestDTO = new BookingDTO(null, ID_CAR_TOYOTA, ID_CUSTOMER, null, null);

        mockMvc.perform(post(URI_PREFIX + "/book")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[*].field", containsInAnyOrder("startDate", "endDate")))
                .andExpect(jsonPath("$.errors[*].message", containsInAnyOrder("must not be null", "must not be null")));
    }

    @Test
    void getBookingById_shouldReturnSavedBooking_whenInputCorrect() throws Exception {
        BookingDTO response = new BookingDTO(ID_BOOKING, ID_CAR_TOYOTA, ID_CUSTOMER,
                LocalDate.of(2126, 4, 23),
                LocalDate.of(2126, 4, 25));

        when(bookingService.getBookingById(ID_BOOKING)).thenReturn(response);

        mockMvc.perform(get(URI_PREFIX + "/bookings/" + ID_STRING_BOOKING))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID_STRING_BOOKING))
                .andExpect(jsonPath("$.vehicleId").value(ID_STRING_CAR_TOYOTA))
                .andExpect(jsonPath("$.customerId").value(ID_STRING_CUSTOMER))
                .andExpect(jsonPath("$.startDate").value("2126-04-23"))
                .andExpect(jsonPath("$.endDate").value("2126-04-25"));
    }
}