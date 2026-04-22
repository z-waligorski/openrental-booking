package com.eprogram.openrental_booking.service;

import com.eprogram.openrental_booking.dto.CarAvailabilityRequestDTO;
import com.eprogram.openrental_booking.dto.CarDTO;
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

    public List<CarDTO> getCarsAvailableForFiltersAndDates(CarAvailabilityRequestDTO dto) {
        List<CarDTO> matchingCars = vehicleService.getFilteredVehiclesIds(dto.brand(), dto.model(),
                dto.minYearOfProduction(), dto.seats());
        Set<UUID> matchingCarsIds = matchingCars.stream()
                .map(CarDTO::id)
                .collect(Collectors.toSet());

        Set<UUID> alreadyBookedIds = bookingRepository.findBookedVehicleIdsInPeriod(matchingCarsIds,
                dto.startDate(),
                dto.endDate());

        matchingCars.removeIf(car -> alreadyBookedIds.contains(car.id()));

        return matchingCars;
    }

}
