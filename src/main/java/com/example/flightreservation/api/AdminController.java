package com.example.flightreservation.api;

import com.example.flightreservation.entities.Flight;
import com.example.flightreservation.openapi.api.AdminApi;
import com.example.flightreservation.openapi.api.model.CreateFlightRequest;
import com.example.flightreservation.openapi.api.model.FlightDto;
import com.example.flightreservation.services.FlightService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class AdminController implements AdminApi {
    private final FlightService flightService;
    private final ModelMapper modelMapper;

    public AdminController(FlightService flightService, ModelMapper modelMapper) {
        this.flightService = flightService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<FlightDto> createFlight(CreateFlightRequest createFlightRequest) {
        Flight flight = flightService.createFlight(createFlightRequest.getDepartureAirportCode(),
                createFlightRequest.getDepartureCity(),
                createFlightRequest.getArrivalAirportCode(),
                createFlightRequest.getArrivalCity(),
                createFlightRequest.getDepartureAt(),
                createFlightRequest.getTotalSeats());
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(flight, FlightDto.class));
    }

    @Override
    public ResponseEntity<Void> deleteFlight(Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }
}
