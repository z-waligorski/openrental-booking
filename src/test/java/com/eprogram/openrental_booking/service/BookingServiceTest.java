package com.eprogram.openrental_booking.service;

import com.eprogram.openrental_booking.dto.BookingDTO;
import com.eprogram.openrental_booking.dto.CarDTO;
import com.eprogram.openrental_booking.exception.booking.BookingNotFoundException;
import com.eprogram.openrental_booking.exception.booking.BookingNotValidException;
import com.eprogram.openrental_booking.exception.integration.VehicleNotFoundException;
import com.eprogram.openrental_booking.mapper.BookingMapper;
import com.eprogram.openrental_booking.model.Booking;
import com.eprogram.openrental_booking.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static com.eprogram.openrental_booking.fixtures.BookingFixtures.*;
import static com.eprogram.openrental_booking.fixtures.CarAvailabilityFixtures.getCarAvailabilityDTOWithCorrectDatesOnly;
import static com.eprogram.openrental_booking.fixtures.CarFixtures.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {
    @Mock
    VehicleService vehicleService;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    BookingMapper bookingMapper;

    @InjectMocks
    BookingService bookingService;

    @Test
    void getCarsAvailableForFiltersAndDates_shouldReturnCars_whenNoneIsBooked() {
        when(vehicleService.getFilteredCars(null, null, null, null))
                .thenReturn(getListOfCars());

        when(bookingRepository.findBookedVehicleIdsInPeriod(
                Set.of(ID_CAR_TOYOTA, ID_CAR_HONDA, ID_CAR_FIAT),
                LocalDate.of(2126, 1, 3),
                LocalDate.of(2126, 1, 7)))
        .thenReturn(Collections.emptySet());

        assertEquals(3, bookingService.getCarsAvailableForFiltersAndDates(getCarAvailabilityDTOWithCorrectDatesOnly()).size());
    }

    @Test
    void getCarsAvailableForFiltersAndDates_shouldReturnEmptyList_whenAllAreBooked() {
        when(vehicleService.getFilteredCars(null, null, null, null))
                .thenReturn(getListOfCars());

        when(bookingRepository.findBookedVehicleIdsInPeriod(
                 Set.of(ID_CAR_TOYOTA, ID_CAR_HONDA, ID_CAR_FIAT),
                LocalDate.of(2126, 1, 3),
                LocalDate.of(2126, 1, 7)))
                .thenReturn(new HashSet<>(Arrays.asList(ID_CAR_TOYOTA, ID_CAR_HONDA, ID_CAR_FIAT)));

        assertEquals(0, bookingService.getCarsAvailableForFiltersAndDates(getCarAvailabilityDTOWithCorrectDatesOnly()).size());
    }

    @Test
    void getCarsAvailableForFiltersAndDates_shouldReturnList_whenPartIsBooked() {
        when(vehicleService.getFilteredCars(null, null, null, null))
                .thenReturn(getListOfCars());

        when(bookingRepository.findBookedVehicleIdsInPeriod(
                Set.of(ID_CAR_TOYOTA, ID_CAR_HONDA, ID_CAR_FIAT),
                LocalDate.of(2126, 1, 3),
                LocalDate.of(2126, 1, 7)))
                .thenReturn(new HashSet<>(Arrays.asList(ID_CAR_TOYOTA, ID_CAR_FIAT)));

        List<CarDTO> result = bookingService.getCarsAvailableForFiltersAndDates(getCarAvailabilityDTOWithCorrectDatesOnly());

        assertEquals(1, result.size());
        assertEquals(ID_CAR_HONDA, result.getFirst().id());
    }

    @Test
    void getCarsAvailableForFiltersAndDates_shouldReturnEmptyList_whenNoCarsFound() {
        when(vehicleService.getFilteredCars(null, null, null, null))
                .thenReturn(Collections.emptyList());

        assertEquals(0, bookingService.getCarsAvailableForFiltersAndDates(getCarAvailabilityDTOWithCorrectDatesOnly()).size());
    }

    @Test
    void validateAndSaveBooking_shouldReturnSavedBookingDTO_whenCarNotBooked() {
        BookingDTO inputDTO = getInputBookingDTO();
        BookingDTO outputDTO = getOutputBookingDTO();

        Booking inputBooking = getInputBooking();
        Booking savedBooking = getOutputBooking();

        when(bookingRepository.findBookedVehicleIdsInPeriod(anyCollection(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.emptySet());
        when(vehicleService.carExists(inputDTO.vehicleId())).thenReturn(true);
        when(bookingMapper.toEntity(inputDTO)).thenReturn(inputBooking);
        when(bookingRepository.save(inputBooking)).thenReturn(savedBooking);
        when(bookingMapper.toDTO(savedBooking)).thenReturn(outputDTO);

        BookingDTO result = bookingService.validateAndSaveBooking(inputDTO);
        assertNotNull(result);
        assertNotNull(result.id());
        assertEquals(ID_CAR_TOYOTA, outputDTO.vehicleId());
        assertEquals(ID_CUSTOMER, outputDTO.customerId());
        assertEquals(LocalDate.of(2126, 3, 5), outputDTO.startDate());
        assertEquals(LocalDate.of(2126, 3, 7), outputDTO.endDate());
    }

    @Test
    void validateAndSaveBooking_shouldThrowException_whenCarBooked() {
        BookingDTO inputDTO = getInputBookingDTO();

        when(bookingRepository.findBookedVehicleIdsInPeriod(List.of(inputDTO.vehicleId()), inputDTO.startDate(), inputDTO.endDate()))
                .thenReturn(Set.of(inputDTO.vehicleId()));

        assertThrows(BookingNotValidException.class, () -> bookingService.validateAndSaveBooking(inputDTO));
        verifyNoInteractions(bookingMapper);
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void validateAndSaveBooking_shouldThrowException_whenVehicleNotFound() {
        BookingDTO inputDTO = getInputBookingDTO();

        when(bookingRepository.findBookedVehicleIdsInPeriod(anyCollection(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.emptySet());
        when(vehicleService.carExists(inputDTO.vehicleId())).thenReturn(false);

        assertThrows(VehicleNotFoundException.class, () -> bookingService.validateAndSaveBooking(inputDTO));
        verifyNoInteractions(bookingMapper);
        verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void getBookingById_shouldReturnBookingDTO_whenFound() {
        Booking booking = getOutputBooking();
        BookingDTO bookingDTO = getOutputBookingDTO();

        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(bookingMapper.toDTO(booking)).thenReturn(bookingDTO);

        assertEquals(bookingDTO, bookingService.getBookingById(booking.getId()));
    }

    @Test
    void getBookingById_shouldThrowException_whenNotFound() {
        when(bookingRepository.findById(GENERATED_ID)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingById(GENERATED_ID),
                "Booking with id " + GENERATED_ID + " not found");
    }
}
