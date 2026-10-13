package com.example.flightreservation.schedulers;

import com.example.flightreservation.services.FlightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class FlightDepartureJob {
    private static final Logger log = LoggerFactory.getLogger(FlightDepartureJob.class);
    private final FlightService flightService;

    public FlightDepartureJob(FlightService flightService) {
        this.flightService = flightService;
    }

    @Scheduled(fixedDelayString = "${flights.expiry.fixed-delay-ms:60000}")
    public void expireHeldBookings() {
        try {
            flightService.markRelevantFlightsAsDeparted();
            log.info("Expired flights processed.");
        } catch (Exception e) {
            log.error("Failed to expire relevant flights.", e);
        }
    }
}