package com.example.flightreservation.exceptions;

public class FlightNotFoundException extends RuntimeException {
    public FlightNotFoundException(Long id) {
        super("Flight not found: " + id);
    }
}