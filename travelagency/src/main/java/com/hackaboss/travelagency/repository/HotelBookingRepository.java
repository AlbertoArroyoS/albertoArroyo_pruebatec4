package com.hackaboss.travelagency.repository;

import com.hackaboss.travelagency.model.Hotel;
import com.hackaboss.travelagency.model.HotelBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long> {

    List<HotelBooking> findByActiveTrue();
    boolean existsByHotelAndActiveTrue(Hotel hotel);
    boolean existsByHotel_Id(Long hotelId);
    @Query(value = "SELECT COUNT(*) FROM hotel_bookings WHERE hotel_id = ?1 AND active = 1", nativeQuery = true)
    Integer countByHotelIdNative(Long hotelId);


    @Query("SELECT CASE WHEN COUNT(hb) > 0 THEN true ELSE false END " +
            "FROM HotelBooking hb " +
            "WHERE hb.hotel = :hotel " +
            "AND hb.hotel.dateFrom = :dateFrom " +
            "AND hb.hotel.dateTo = :dateTo " +
            "AND hb.active = true")
    boolean existsByHotelAndDateFromAndDateToAndActiveTrue(@Param("hotel") Hotel hotel,
                                                           @Param("dateFrom") LocalDate dateFrom,
                                                           @Param("dateTo") LocalDate dateTo);

}
