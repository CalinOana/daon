package com.example.flightreservation.api;

import com.example.flightreservation.entities.Flight;
import com.example.flightreservation.openapi.api.AdminApi;
import com.example.flightreservation.openapi.api.model.CreateFlightRequest;
import com.example.flightreservation.openapi.api.model.FlightDto;
import com.example.flightreservation.services.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class AdminController implements AdminApi {
    private final FlightService flightService;

    public AdminController(FlightService flightService) {
        this.flightService = flightService;
    }

    @Override
    public ResponseEntity<FlightDto> createFlight(CreateFlightRequest createFlightRequest) {
        Flight flight = flightService.createFlight(createFlightRequest.getDepartureAirportCode(),
                createFlightRequest.getDepartureCity(),
                createFlightRequest.getArrivalAirportCode(),
                createFlightRequest.getArrivalCity(),
                createFlightRequest.getDepartureAt(),
                createFlightRequest.getTotalSeats());
        return ResponseEntity.status(HttpStatus.CREATED).body(toFlightDto(flight));
    }

    @Override
    public ResponseEntity<Void> deleteFlight(Long id) {
        return null;
    }

    private FlightDto toFlightDto(Flight flight) {
        FlightDto dto = new FlightDto();
        dto.setId(flight.getId());
        dto.setDepartureAirportCode(flight.getDepartureAirportCode());
        dto.setDepartureCity(flight.getDepartureCity());
        dto.setArrivalAirportCode(flight.getArrivalAirportCode());
        dto.setArrivalCity(flight.getArrivalCity());
        dto.setDepartureAt(flight.getDepartureAt());
        dto.setTotalSeats(flight.getTotalSeats());
        dto.setRemainingSeats(flight.getRemainingSeats());
        dto.setStatus(FlightDto.StatusEnum.fromValue(flight.getStatus().name()));
        return dto;
    }
}
