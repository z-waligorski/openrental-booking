package com.eprogram.openrental_booking.service;

import com.eprogram.openrental_booking.dto.CarDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class VehicleService {

    // TODO implement real logic
    public List<CarDTO> getFilteredVehiclesIds(String brand,
                                          String model,
                                          Integer minYearOfProduction,
                                          Integer seats) {
        return new ArrayList<>(List.of(getCarDTO("Toyota", "Corolla", 5, "edbc3d3a-b519-465d-bb52-76c18a7e0d7f"), // mock implementation
                getCarDTO("Honda", "Civic", 5, "6594fe88-a5ad-4530-9f71-8191e0781ed3"),
                getCarDTO("Opel", "Corsa", 4, "c34d7232-1bc1-4afd-b5b5-a876e8a1533f"))
        );
    }

    CarDTO getCarDTO(String brand, String model, Integer seats, String uuid) {
        return new CarDTO(UUID.fromString(uuid),
                brand,
                model,
                2020,
                seats);
    }
}
