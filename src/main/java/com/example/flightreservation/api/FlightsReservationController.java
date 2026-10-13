package com.example.flightreservation.api;

import com.example.flightreservation.entities.Booking;
import com.example.flightreservation.openapi.api.BookingsApi;
import com.example.flightreservation.openapi.api.model.BookingDto;
import com.example.flightreservation.openapi.api.model.CreateBookingRequest;
import com.example.flightreservation.services.BookingService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class FlightsReservationController implements BookingsApi {
    final BookingService bookingService;
    final ModelMapper modelMapper;

    public FlightsReservationController(BookingService bookingService, ModelMapper modelMapper) {
        this.bookingService = bookingService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<Void> cancelBooking(Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<BookingDto> confirmBooking(Long id) {
        Booking booking = bookingService.confirmBooking(id);
        return ResponseEntity.ok(modelMapper.map(booking, BookingDto.class));
    }

    @Override
    public ResponseEntity<BookingDto> createBooking(Long id, CreateBookingRequest createBookingRequest) {
        Booking booking = bookingService.createBooking(
                id,
                createBookingRequest.getPassengerName(),
                createBookingRequest.getPassengerEmail()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(booking, BookingDto.class));
    }

    @Override
    public ResponseEntity<List<BookingDto>> listBookings(Long id) {
        return ResponseEntity.ok(
                List.of(modelMapper.map(bookingService.listBookings(id), BookingDto[].class))
        );
    }
}
