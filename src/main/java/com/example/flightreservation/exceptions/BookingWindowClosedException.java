package com.example.flightreservation.exceptions;

public class BookingWindowClosedException extends RuntimeException {
    public BookingWindowClosedException(String message) {
        super("Booking time has expired for flight as there are less than 45 mins before it departs" + message);
    }
}