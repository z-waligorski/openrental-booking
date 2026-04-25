package com.eprogram.openrental_booking.service;

import com.eprogram.openrental_booking.dto.CarDTO;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class VehicleService {

    private final RestClient restClient;

    public List<CarDTO> getFilteredCars(String brand,
                                        String model,
                                        Integer minYearOfProduction,
                                        Integer seats) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/cars/filter")
                        .queryParamIfPresent("brand", Optional.ofNullable(brand))
                        .queryParamIfPresent("model", Optional.ofNullable(model))
                        .queryParamIfPresent("minYearOfProduction", Optional.ofNullable(minYearOfProduction))
                        .queryParamIfPresent("seats", Optional.ofNullable(seats))
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public boolean carExists(@NotNull UUID uuid) {
        try {
            restClient.get()
                    .uri("/cars/{id}", uuid)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        }
    }
}
