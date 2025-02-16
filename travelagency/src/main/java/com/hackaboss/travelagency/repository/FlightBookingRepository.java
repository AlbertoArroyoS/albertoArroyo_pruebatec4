package com.hackaboss.travelagency.repository;

import com.hackaboss.travelagency.model.Flight;
import com.hackaboss.travelagency.model.FlightBooking;
import com.hackaboss.travelagency.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightBookingRepository extends JpaRepository<FlightBooking, Long> {

    List<FlightBooking> findByActiveTrue();

    boolean existsByFlightAndPassengersInAndActiveTrue(Flight flight, List<User> passengers);

}
