package com.hackaboss.travelagency.repository;

import com.hackaboss.travelagency.model.Hotel;
import com.hackaboss.travelagency.util.Booked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByActiveTrue();

    Optional<Hotel> findByIdAndActiveTrue(Long id);

    Optional<Hotel> findByHotelCodeAndActiveTrue(String hotelCode);

    Optional<Hotel> findByHotelCode(String hotelCode);

    List<Hotel> findByCityAndBookedNot(String city, Booked booked);

    List<Hotel> findByCityAndBookedAndDateFromLessThanEqualAndDateToGreaterThanEqualAndActiveTrue(
            String city,
            Booked booked,
            LocalDate requestDateFrom,
            LocalDate requestDateTo
    );

    Optional<Hotel> findByHotelCodeAndName(String hotelCode, String name);

}
