package com.example.flightreservation.api;

import com.example.flightreservation.entities.Flight;
import com.example.flightreservation.openapi.api.FlightsApi;
import com.example.flightreservation.openapi.api.model.FlightDto;
import com.example.flightreservation.services.FlightService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Controller
public class FlightsController implements FlightsApi {

    private final FlightService flightService;
    private final ModelMapper modelMapper;

    public FlightsController(FlightService flightService, ModelMapper modelMapper) {
        this.flightService = flightService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<List<FlightDto>> getFlights(
            @Parameter(name = "date", description = "Filter by departure date in ISO-8601 yyyy-MM-dd", in = ParameterIn.QUERY) @Valid @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Size(min = 3, max = 10) @Parameter(name = "from", description = "Departure airport or city code", in = ParameterIn.QUERY) @Valid @RequestParam(value = "from", required = false) String from,
            @Size(min = 3, max = 10) @Parameter(name = "to", description = "Arrival airport or city code", in = ParameterIn.QUERY) @Valid @RequestParam(value = "to", required = false) String to
    ) {
        List<Flight> flights = flightService.getFlights(date == null ? Optional.empty() : Optional.of(date.toString()), from == null ? Optional.empty() : Optional.of(from), to == null ? Optional.empty() : Optional.of(to));

        List<FlightDto> dtoList = flights.stream().map(flight -> modelMapper.map(modelMapper.map(flight, FlightDto.class), FlightDto.class)).toList();

        return ResponseEntity.ok(dtoList);
    }
}
