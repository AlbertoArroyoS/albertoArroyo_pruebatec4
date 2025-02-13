package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.response.HotelDTOResponse;

import java.util.List;

public interface IHotelService {

    List<HotelDTOResponse> findAll();

}
