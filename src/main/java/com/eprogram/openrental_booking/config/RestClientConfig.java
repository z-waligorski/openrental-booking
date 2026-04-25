package com.eprogram.openrental_booking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    RestClient vehiclesRestClient(
            RestClient.Builder builder,
            VehiclesServiceProperties properties) {
        return builder.baseUrl(properties.url())
                .build();
    }
}
