package com.hackaboss.travelagency.repository;

import com.hackaboss.travelagency.model.HotelBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long> {
}
