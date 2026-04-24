package com.eprogram.openrental_booking.mapper;

import com.eprogram.openrental_booking.dto.BookingDTO;
import com.eprogram.openrental_booking.model.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public Booking toEntity(BookingDTO dto) {
        Booking booking = new Booking();
        booking.setId(dto.id());
        booking.setCustomerId(dto.customerId());
        booking.setVehicleId(dto.vehicleId());
        booking.setStartDate(dto.startDate());
        booking.setEndDate(dto.endDate());
        return booking;
    }

    public BookingDTO toDTO(Booking entity) {
        return new BookingDTO(entity.getId(),
                entity.getCustomerId(),
                entity.getVehicleId(),
                entity.getStartDate(),
                entity.getEndDate());
    }
}
