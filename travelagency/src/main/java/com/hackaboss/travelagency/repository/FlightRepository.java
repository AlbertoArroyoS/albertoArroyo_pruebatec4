package com.hackaboss.travelagency.repository;

import com.hackaboss.travelagency.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByOriginAndDestinationAndDepartureDateAndReturnDateAndActiveTrue(
            String origin,
            String destination,
            LocalDate departureDate,
            LocalDate returnDate
    );

    Optional<Flight> findByIdAndActiveTrue(Long id);
    Optional<Flight> findByFlightNumberAndActiveTrue(String flightNumber);
    List<Flight> findByActiveTrue();
}
