package com.hackaboss.travelagency.repository;

import com.hackaboss.travelagency.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByActiveTrue();

    Optional<Hotel> findByIdAndActiveTrue(Long id);

    Optional<Hotel> findByHotelCodeAndActiveTrue(String hotelCode);



}
