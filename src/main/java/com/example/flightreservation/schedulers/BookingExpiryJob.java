package com.example.flightreservation.schedulers;

import com.example.flightreservation.services.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component
@EnableScheduling
public class BookingExpiryJob {
    private static final Logger log = LoggerFactory.getLogger(BookingExpiryJob.class);
    private final BookingService bookingService;
    public BookingExpiryJob(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    @Scheduled(fixedDelayString = "${booking.expiry.fixed-delay-ms:60000}")
    public void expireHeldBookings() {
        try {
            bookingService.expireHeldBookings();
            log.info("Expired held bookings processed.");
        } catch (Exception e) {
            log.error("Failed to expire held bookings.", e);
        }
    }
}