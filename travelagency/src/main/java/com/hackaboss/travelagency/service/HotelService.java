package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.response.HotelDTOResponse;
import com.hackaboss.travelagency.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService implements IHotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }



    @Override
    public List<HotelDTOResponse> findAll() {
        return hotelRepository.findAll();
    }
}

