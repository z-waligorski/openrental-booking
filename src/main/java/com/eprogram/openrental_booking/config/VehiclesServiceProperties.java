package com.eprogram.openrental_booking.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "services.vehicles")
public record VehiclesServiceProperties(String url) {
}
