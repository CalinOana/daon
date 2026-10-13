package com.example.flightreservation.entities;

import com.example.flightreservation.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;
    @Column(name = "passenger_name", nullable = false, length = 150)
    private String passengerName;
    @Column(name = "passenger_email", nullable = false)
    private String passengerEmail;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;
    @Setter
    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;
    @Setter
    @Column(name = "confirmed_at")
    private OffsetDateTime confirmedAt;
    @Setter
    @Column(name = "cancelled_at")
    private OffsetDateTime cancelledAt;
    @Version
    @Column(nullable = false)
    private Long version;
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected Booking() {
    }

    public Booking(Flight flight, String passengerName, String passengerEmail, OffsetDateTime expiresAt) {
        this.flight = flight;
        this.passengerName = passengerName;
        this.passengerEmail = passengerEmail;
        this.status = BookingStatus.HELD;
        this.expiresAt = expiresAt;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
        this.updatedAt = OffsetDateTime.now();
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