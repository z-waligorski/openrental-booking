package com.eprogram.openrental_booking.service;

import com.eprogram.openrental_booking.dto.CarDTO;
import com.eprogram.openrental_booking.exception.integration.VehiclesServiceInvalidRequestException;
import com.eprogram.openrental_booking.exception.integration.VehiclesServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@CircuitBreaker(name = "vehicles-service-circuit-breaker")
@Retry(name = "vehicles-service-retry")
@Service
public class VehicleService {

    private final RestClient restClient;

    public List<CarDTO> getFilteredCars(String brand,
                                        String model,
                                        Integer minYearOfProduction,
                                        Integer seats) {
        try {
            log.info("Calling Vehicles service for filtering cars");
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
        } catch (HttpClientErrorException e) {
            log.warn("Request to vehicle service not valid", e);
            throw new VehiclesServiceInvalidRequestException();
        } catch (HttpServerErrorException e) {
            log.warn("Vehicle service returned server error", e);
            throw new VehiclesServiceUnavailableException();
        } catch (ResourceAccessException e) {
            log.warn("Error while calling Vehicles service for filtering cars", e);
            throw new VehiclesServiceUnavailableException();
        }
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
        } catch (HttpServerErrorException e) {
            log.warn("Vehicle service returned server error", e);
            throw new VehiclesServiceUnavailableException();
        } catch (ResourceAccessException e) {
            log.warn("Error while calling Vehicles service when checking car existence", e);
            throw new VehiclesServiceUnavailableException();
        }
    }
}
