package com.eprogram.openrental_booking.service;

import com.eprogram.openrental_booking.dto.BookingDTO;
import com.eprogram.openrental_booking.dto.CarAvailabilityRequestDTO;
import com.eprogram.openrental_booking.dto.CarDTO;
import com.eprogram.openrental_booking.exception.BookingNotFoundException;
import com.eprogram.openrental_booking.exception.BookingNotValidException;
import com.eprogram.openrental_booking.mapper.BookingMapper;
import com.eprogram.openrental_booking.model.Booking;
import com.eprogram.openrental_booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BookingService {
    private final VehicleService vehicleService;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    public List<CarDTO> getCarsAvailableForFiltersAndDates(CarAvailabilityRequestDTO dto) {
        List<CarDTO> matchingCars = vehicleService.getFilteredVehiclesIds(dto.brand(), dto.model(),
                dto.minYearOfProduction(), dto.seats());
        if (matchingCars.isEmpty()) {
            return matchingCars;
        }
        Set<UUID> matchingCarsIds = matchingCars.stream()
                .map(CarDTO::id)
                .collect(Collectors.toSet());

        Set<UUID> alreadyBookedIds = bookingRepository.findBookedVehicleIdsInPeriod(matchingCarsIds,
                dto.startDate(),
                dto.endDate());

        matchingCars.removeIf(car -> alreadyBookedIds.contains(car.id()));

        return matchingCars;
    }

    public BookingDTO validateAndSaveBooking(BookingDTO bookingDTO) {
        Set<UUID> alreadyBookedIds = bookingRepository.findBookedVehicleIdsInPeriod(List.of(bookingDTO.vehicleId()),
                bookingDTO.startDate(),
                bookingDTO.endDate());
        if(!alreadyBookedIds.isEmpty()) {
            throw new BookingNotValidException();
        }
        // TODO check also if vehicle id is valid
        Booking savedBooking = bookingRepository.save(bookingMapper.toEntity(bookingDTO));
        return bookingMapper.toDTO(savedBooking);
    }

    public BookingDTO getBookingById(UUID id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException(id));
        return bookingMapper.toDTO(booking);
    }

}
