package com.eprogram.openrental_booking.repository;

import com.eprogram.openrental_booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    @Query("""
                    SELECT DISTINCT b.vehicleId
                    FROM Booking b
                    WHERE b.vehicleId in:vehicleIds
                    and b.startDate <= :endDate
                    AND b.endDate >= :startDate
            """)
    Set<UUID> findBookedVehicleIdsInPeriod(@Param("vehicleIds") Collection<UUID> vehicleIds,
                                         @Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);
}
