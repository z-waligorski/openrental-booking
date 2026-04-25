package com.eprogram.openrental_booking;

import com.eprogram.openrental_booking.config.VehiclesServiceProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(VehiclesServiceProperties.class)
@SpringBootApplication
public class OpenrentalBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenrentalBookingApplication.class, args);
	}

}
