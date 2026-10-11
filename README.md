# Flight reservation system

## Reqs:

### Endpoints

| Endpoint | Route                  | Purpose                                                      |
|----------|------------------------|--------------------------------------------------------------|
| GET      | /flights               | List available flights, optionally filtered by date or route |
| POST     | /flights/{id}/bookings | Reserve a seat on a flight for a passenger                   |
| POST     | /bookings/{id}/confirm | Confirm and finalise a reservation                           |
| DELETE   | /bookings/{id}         | Cancel a reservation and release the seat                    |
| POST     | /admin/flights         | Create a new flight in the system                            |
| DELETE   | /admin/flights/{id}    | Remove a flight from the system                              |

### Booking window
Each flight departs from a specific airport in a specific city. Bookings for a flight must be refused
if the request comes in less than 45 minutes before the scheduled departure
For example, a flight departing Dublin at 14:30 local time should stop accepting bookings at
13:45 Dublin time.

### Seat holds

When a booking is created it should initially be in a held state, pending confirmation. Held seats
that are never confirmed should eventually be released so that other passengers can book
them. How you implement this — and how long a seat stays held — is up to you.
### Seat availability
The system should be resilient to double booking or overselling seats.
### Deliverables
Please share a Git repository (public, or invite us) containing:
• Working, runnable code
• A README that covers how to run the service, the key design decisions you made, and
any trade-offs or limitations you're aware of
A Dockerfile or simple startup script is a nice-to-have but not required.

# Design
-Flight pojo :  id, from, to, time_of_departure, nr_of_left_seats
-Bookin g: id, flight_id, time_of_expiry, confirmed/held


## Flight lifecycle:

-Creation: POST endpoint
-Deletion: DELETE endpoint
-Expiry	 : time_of_departure expired -> delete or set a departed flag

## Booking lifecycle

- Reservation	: 	POST endpoint 	-> new booking with HELD status, refused if less than 45 mins
- Confirmation	:	POST endpoint 	-> old booking updated with CONFIRMED status, refused if less than 45 mins
- Deletion		:	DELETE endpoint	-> deleted regardless of state
- Expiry			:	no endpoint		-> deleted if HELD

## Dealing with time concerns:

- **Batching** (start timer of reservation holding/flight)
- **Timekeeping** function for EVERYTHING (function at the beginning of ALL interactions with endpoints, maybe annotation, AOP (maybe too complex for the demonstration))

## Resiliency to double booking:

Weaknesses:
-  HOLD spam
-  CONFIRM spam
-  Overselling

Solutions:
- Reservation queue
