package com.example.flightreservation.repositories;


import com.example.flightreservation.entities.Booking;
import com.example.flightreservation.enums.BookingStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Booking b where b.id = :id")
    Optional<Booking> findByIdForUpdate(@Param("id") Long id);

    @Query("""
            select b
            from Booking b
            where b.status = 'HELD'
            and b.expiresAt <= :now
            """)
    List<Booking> findExpiredHeldBookings(@Param("now") OffsetDateTime now);

    @Query("""
            select count(b)
            from Booking b
            where b.flight.id = :flightId
            and b.status = :status
            """)
    long countByFlightIdAndStatus(@Param("flightId") Long flightId, @Param("status") BookingStatus status);

    List<Booking> findByFlightId(Long flightId);
}