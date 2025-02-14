package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.HotelBookingDTORequest;
import com.hackaboss.travelagency.dto.response.HotelBookingDTOResponse;

import java.util.List;

public interface IHotelBookingService {

    List<HotelBookingDTOResponse> findAll();
    String createHotelBooking(HotelBookingDTORequest hotelBookingDTORequest);

}
