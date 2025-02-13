package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.HotelDTORequest;
import com.hackaboss.travelagency.dto.response.HotelDTOResponse;
import com.hackaboss.travelagency.mapper.HotelBookingMapper;
import com.hackaboss.travelagency.mapper.HotelMapper;
import com.hackaboss.travelagency.model.Hotel;
import com.hackaboss.travelagency.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService implements IHotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Autowired
    public HotelService(HotelRepository hotelRepository, HotelMapper hotelMapper) {
        this.hotelRepository = hotelRepository;
        this.hotelMapper = hotelMapper;
    }


    @Override
    public List<HotelDTOResponse> findAll() {
        return hotelRepository.findAll().stream()
                .map(hotelMapper::entityToDTO)
                .toList();
    }

    @Override
    public String createHotel(HotelDTORequest hotelDTORequest) {
        //Convertir el DTO a entidad y guardarla
        Hotel hotel = hotelMapper.requestToEntity(hotelDTORequest);
        hotelRepository.save(hotel);
        return "Hotel creado con éxito";

    }
}

