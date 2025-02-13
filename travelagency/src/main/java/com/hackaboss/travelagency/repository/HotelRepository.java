package com.hackaboss.travelagency.repository;

import com.hackaboss.travelagency.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
