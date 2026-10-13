INSERT INTO flight (
    departure_airport_code, departure_city, arrival_airport_code, arrival_city,
    departure_at, total_seats, remaining_seats, status
)
VALUES
    ('DUB', 'Dublin', 'LHR', 'London', TIMESTAMP WITH TIME ZONE '2027-01-15T14:30:00Z', 180, 180, 'ACTIVE'),
    ('CDG', 'Paris', 'JFK', 'New York', TIMESTAMP WITH TIME ZONE '2027-01-20T09:15:00Z', 120, 120, 'ACTIVE');
--     ('SBZ', 'Sibiu', 'LAX', 'Los Angeles', ABS(CURRENT_TIME + INTERVAL '1' MINUTE ), 200, 200, 'ACTIVE'), --intentionally set to 30 minutes from now for testing purposes
--     ('OTP', 'Bucharest', 'LAX', 'Los Angeles',( CURRENT_TIME - INTERVAL '30' MINUTE ), 200, 200, 'ACTIVE'); --intentionally set to 30 minutes before for testing purposes

-- INSERT INTO booking (
--     flight_id, passenger_name, passenger_email, expires_at, status
-- )
-- VALUES
--     (1, 'John Doe', 'john@doe.com', ABS(CURRENT_TIME + INTERVAL '1' MINUTE),'HELD' );