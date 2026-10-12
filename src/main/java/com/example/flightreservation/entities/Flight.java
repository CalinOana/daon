package com.example.flightreservation.entities;
import com.example.flightreservation.enums.FlightStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "flight")
@Data
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Column(name = "departure_airport_code", nullable = false, length = 10)
    private String departureAirportCode;
    @Getter
    @Column(name = "departure_city", nullable = false, length = 100)
    private String departureCity;
    @Getter
    @Column(name = "arrival_airport_code", nullable = false, length = 10)
    private String arrivalAirportCode;
    @Getter
    @Column(name = "arrival_city", nullable = false, length = 100)
    private String arrivalCity;
    @Getter
    @Column(name = "departure_at", nullable = false)
    private OffsetDateTime departureAt;
    @Getter
    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;
    @Getter
    @Column(name = "remaining_seats", nullable = false)
    private Integer remainingSeats;
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FlightStatus status;
    @Getter
    @Version
    @Column(nullable = false)
    private Long version;
    @Getter
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
    @Getter
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
    @Getter
    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();
    protected Flight() {
    }
    public Flight(
            String departureAirportCode,
            String departureCity,
            String arrivalAirportCode,
            String arrivalCity,
            OffsetDateTime departureAt,
            Integer totalSeats
    ) {
        this.departureAirportCode = departureAirportCode;
        this.departureCity = departureCity;
        this.arrivalAirportCode = arrivalAirportCode;
        this.arrivalCity = arrivalCity;
        this.departureAt = departureAt;
        this.totalSeats = totalSeats;
        this.remainingSeats = totalSeats;
        this.status = FlightStatus.ACTIVE;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public void setStatus(FlightStatus status) { this.status = status; this.updatedAt = OffsetDateTime.now(); }
    public void setRemainingSeats(Integer remainingSeats) {
        this.remainingSeats = remainingSeats;
        this.updatedAt = OffsetDateTime.now();
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.setFlight(this);
    }
    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = OffsetDateTime.now();
        if (updatedAt == null) updatedAt = createdAt;
    }
    @PreUpdate
    void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}