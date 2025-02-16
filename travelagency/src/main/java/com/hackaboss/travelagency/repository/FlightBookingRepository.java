package com.hackaboss.travelagency.repository;

import com.hackaboss.travelagency.model.FlightBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightBookingRepository extends JpaRepository<FlightBooking, Long> {

    List<FlightBooking> findByActiveTrue();
}
