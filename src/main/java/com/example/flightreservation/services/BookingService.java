package com.example.flightreservation.services;


import com.example.flightreservation.entities.Booking;
import com.example.flightreservation.entities.Flight;
import com.example.flightreservation.enums.BookingStatus;
import com.example.flightreservation.enums.FlightStatus;

import com.example.flightreservation.exceptions.BookingNotFoundException;
import com.example.flightreservation.exceptions.BookingWindowClosedException;
import com.example.flightreservation.exceptions.InvalidBookingStateException;
import com.example.flightreservation.exceptions.SeatUnavailableException;
import com.example.flightreservation.repositories.BookingRepository;
import com.example.flightreservation.repositories.FlightRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class BookingService {
    private final long bookingWindowMinutes;
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;
    private final static int BOOKING_EXPIRY_WINDOW = 15;

    public BookingService(FlightRepository flightRepository, BookingRepository bookingRepository, @Value("${flights.booking.window.closing-minutes:45}") String bookingWindowMinutes) {
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
        this.bookingWindowMinutes = Long.parseLong(bookingWindowMinutes);
    }

    @Transactional
    public Booking createBooking(Long flightId, String passengerName, String passengerEmail) {
        OffsetDateTime now = OffsetDateTime.now();
        Flight flight = flightRepository.findByIdForUpdate(flightId)
                .orElseThrow(() -> new IllegalArgumentException("Flight not found: " + flightId));
        if (flight.getStatus() != FlightStatus.ACTIVE) {
            throw new BookingWindowClosedException("Flight is no longer accepting bookings.");
        }
        validateBookingWindow(flight, now);
        if (flight.getRemainingSeats() <= 0) {
            throw new SeatUnavailableException("No seats available on this flight.");
        }
        OffsetDateTime expiresAt = now.plusMinutes(BOOKING_EXPIRY_WINDOW);
        Booking booking = new Booking(flight, passengerName, passengerEmail, expiresAt);
        bookingRepository.save(booking);
        flight.setRemainingSeats(flight.getRemainingSeats() - 1);
        flightRepository.save(flight);
        return booking;
    }

    @Transactional
    public Booking confirmBooking(Long bookingId) {
        OffsetDateTime now = OffsetDateTime.now();
        Booking booking = bookingRepository.findByIdForUpdate(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
        Flight flight = booking.getFlight();
        if (booking.getStatus() != BookingStatus.HELD) {
            throw new InvalidBookingStateException("Only HELD bookings can be confirmed.");
        }
        if (flight.getStatus() != FlightStatus.ACTIVE) {
            throw new BookingWindowClosedException("Flight is not active; booking cannot be confirmed.");
        }
        if (booking.getExpiresAt().isBefore(now) || booking.getExpiresAt().isEqual(now)) {
            expireHeldBooking(booking);
            throw new BookingWindowClosedException("Booking hold has expired.");
        }
        validateBookingWindow(flight, now);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setConfirmedAt(now);
        bookingRepository.save(booking);
        return booking;
    }

    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findByIdForUpdate(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.EXPIRED) {
            return;
        }
        Flight flight = booking.getFlight();
        if (booking.getStatus() == BookingStatus.HELD || booking.getStatus() == BookingStatus.CONFIRMED) {
            flight.setRemainingSeats(flight.getRemainingSeats() + 1);
        }
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(OffsetDateTime.now());
        bookingRepository.save(booking);
        flightRepository.save(flight);
    }

    @Transactional
    public void expireHeldBookings() {
        OffsetDateTime now = OffsetDateTime.now();
        List<Booking> expired = bookingRepository.findExpiredHeldBookings(now);
        for (Booking booking : expired) {
            expireHeldBooking(booking);
        }
    }

    private void expireHeldBooking(Booking booking) {
        if (booking.getStatus() != BookingStatus.HELD) {
            return;
        }
        Flight flight = booking.getFlight();
        flight.setRemainingSeats(flight.getRemainingSeats() + 1);
        booking.setStatus(BookingStatus.EXPIRED);
        bookingRepository.save(booking);
        flightRepository.save(flight);
    }

    private void validateBookingWindow(Flight flight, OffsetDateTime now) {
        Duration remaining = Duration.between(now, flight.getDepartureAt());
        if (remaining.compareTo(Duration.ofMinutes(bookingWindowMinutes)) < 0) {
            throw new BookingWindowClosedException(
                    "Bookings close " + bookingWindowMinutes + " minutes before departure; flight departs at " + flight.getDepartureAt()
            );
        }
    }

    public List<Booking> listBookings(Long flightId
    ) {
        return bookingRepository.findByFlightId(flightId);
    }
}