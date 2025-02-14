package com.hackaboss.travelagency.repository;

import com.hackaboss.travelagency.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Optional<Hotel> findByHotelCode(String hotelCode);
}
