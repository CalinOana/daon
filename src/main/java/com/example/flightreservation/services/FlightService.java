package com.example.flightreservation.services;

import com.example.flightreservation.entities.Flight;
import com.example.flightreservation.enums.FlightStatus;
import com.example.flightreservation.exceptions.FlightNotFoundException;
import com.example.flightreservation.repositories.FlightRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {
    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Transactional
    public Flight createFlight(
            String departureAirportCode,
            String departureCity,
            String arrivalAirportCode,
            String arrivalCity,
            OffsetDateTime departureAt,
            Integer totalSeats
    ) {
        if (departureAt == null) throw new IllegalArgumentException("departureAt is required");
        if (totalSeats == null || totalSeats <= 0) throw new IllegalArgumentException("totalSeats must be > 0");
        Flight flight = new Flight(
                departureAirportCode,
                departureCity,
                arrivalAirportCode,
                arrivalCity,
                departureAt,
                totalSeats
        );
        return flightRepository.save(flight);
    }

    @Transactional
    public void deleteFlight(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException(flightId));
        flightRepository.delete(flight);
    }

    @Transactional()
    public List<Flight> getFlights(Optional<String> date, Optional<String> from, Optional<String> to) {
        OffsetDateTime now = OffsetDateTime.now();
        List<Flight> flights = flightRepository.findUpcomingActiveFlights(now);
        if (date.isPresent()) {
            flights = flights.stream()
                    .filter(f -> f.getDepartureAt().toLocalDate().toString().equals(date.get()))
                    .toList();
        }
        if (from.isPresent()) {
            String origin = from.get();
            flights = flights.stream()
                    .filter(f -> f.getDepartureAirportCode().equalsIgnoreCase(origin)
                            || f.getDepartureCity().equalsIgnoreCase(origin))
                    .toList();
        }
        if (to.isPresent()) {
            String destination = to.get();
            flights = flights.stream()
                    .filter(f -> f.getArrivalAirportCode().equalsIgnoreCase(destination)
                            || f.getArrivalCity().equalsIgnoreCase(destination))
                    .toList();
        }
        return flights;
    }

    public void markFlightDeparted(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException(flightId));
        flight.setStatus(FlightStatus.DEPARTED);
    }

    @Transactional
    public void cancelFlight(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new FlightNotFoundException(flightId));
        flight.setStatus(FlightStatus.CANCELLED);
    }

    @Transactional
    public void markRelevantFlightsAsDeparted() {
        flightRepository.findAll().forEach(flight -> {
            if (flight.getStatus() == FlightStatus.ACTIVE && flight.getDepartureAt().isBefore(OffsetDateTime.now())) {
                markFlightDeparted(flight.getId());
            }
        });
    }
}