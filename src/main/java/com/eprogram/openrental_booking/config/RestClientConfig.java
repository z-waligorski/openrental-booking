package com.eprogram.openrental_booking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpClientSettings;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Bean
    RestClient vehiclesRestClient(
            RestClient.Builder builder,
            VehiclesServiceProperties properties) {
        return builder.baseUrl(properties.url())
                .requestFactory(customRequestFactory())
                .build();
    }

    ClientHttpRequestFactory customRequestFactory() {
        HttpClientSettings settings = HttpClientSettings.defaults()
                .withConnectTimeout(Duration.ofSeconds(2))
                .withReadTimeout(Duration.ofSeconds(3));

        return ClientHttpRequestFactoryBuilder.detect()
                .build(settings);
    }
}
