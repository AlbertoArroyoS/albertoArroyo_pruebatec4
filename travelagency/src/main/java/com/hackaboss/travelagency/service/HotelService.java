package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.response.HotelBookingDTOResponse;
import com.hackaboss.travelagency.mapper.HotelBookingMapper;
import com.hackaboss.travelagency.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService implements IHotelService {

    private final HotelRepository hotelRepository;
    private final HotelBookingMapper hotelBookingMapper;

    @Autowired
    public HotelService(HotelRepository hotelRepository, HotelBookingMapper hotelBookingMapper) {
        this.hotelRepository = hotelRepository;
        this.hotelBookingMapper = hotelBookingMapper;
    }


    @Override
    public List<HotelBookingDTOResponse> findAll() {
        return hotelRepository.findAll().stream()
                .map(hotelBookingMapper::toDTO)
                .toList();
    }
}

