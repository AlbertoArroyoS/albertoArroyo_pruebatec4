package com.hackaboss.travelagency.repository;

import com.hackaboss.travelagency.model.FlightBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightBookingRepository extends JpaRepository<FlightBooking, Long> {
}
