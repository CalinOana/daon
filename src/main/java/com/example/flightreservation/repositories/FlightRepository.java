package com.example.flightreservation.repositories;

import com.example.flightreservation.entities.Flight;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select f from Flight f where f.id = :id")
    Optional<Flight> findByIdForUpdate(@Param("id") Long id);

    @Query("""
            select f
            from Flight f
            where f.status = 'ACTIVE'
            and f.departureAt > :now
            order by f.departureAt asc
            """)
    List<Flight> findUpcomingActiveFlights(@Param("now") OffsetDateTime now);
}
