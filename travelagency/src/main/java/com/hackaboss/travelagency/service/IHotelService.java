package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.response.HotelBookingDTOResponse;

import java.util.List;

public interface IHotelService {

    List<HotelBookingDTOResponse> findAll();

}
