package com.hackaboss.travelagency.repository;

import com.hackaboss.travelagency.model.Hotel;
import com.hackaboss.travelagency.model.HotelBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long> {

    List<HotelBooking> findByActiveTrue();

    boolean existsByHotelAndDateFromAndDateToAndActiveTrue(Hotel hotel, LocalDate dateFrom, LocalDate dateTo);
    boolean existsByHotelAndActiveTrue(Hotel hotel);

}
