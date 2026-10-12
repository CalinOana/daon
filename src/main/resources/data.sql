INSERT INTO flight (
    departure_airport_code, departure_city, arrival_airport_code, arrival_city,
    departure_at, total_seats, remaining_seats, status
)
VALUES
    ('DUB', 'Dublin', 'LHR', 'London', TIMESTAMP WITH TIME ZONE '2027-01-15T14:30:00Z', 180, 180, 'ACTIVE'),
    ('CDG', 'Paris', 'JFK', 'New York', TIMESTAMP WITH TIME ZONE '2027-01-20T09:15:00Z', 120, 120, 'ACTIVE');