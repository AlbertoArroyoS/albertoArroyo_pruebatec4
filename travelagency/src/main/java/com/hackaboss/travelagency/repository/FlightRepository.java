package com.hackaboss.travelagency.repository;

import com.hackaboss.travelagency.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {
}
