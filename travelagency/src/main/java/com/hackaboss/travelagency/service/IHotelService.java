package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.HotelDTORequest;
import com.hackaboss.travelagency.dto.response.HotelDTOResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IHotelService {

    List<HotelDTOResponse> findAll();
    String createHotel (HotelDTORequest hotelDTORequest);
    Optional<HotelDTOResponse> findById(Long id);
    List<HotelDTOResponse> findAvailableRooms(String destination, LocalDate requestDateFrom, LocalDate requestDateTo);
}
