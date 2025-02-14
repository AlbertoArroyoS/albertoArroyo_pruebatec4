package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.HotelBookingDTORequest;
import com.hackaboss.travelagency.dto.response.HotelBookingDTOResponse;
import com.hackaboss.travelagency.mapper.HotelBookingMapper;
import com.hackaboss.travelagency.model.Hotel;
import com.hackaboss.travelagency.model.HotelBooking;
import com.hackaboss.travelagency.model.User;
import com.hackaboss.travelagency.repository.HotelBookingRepository;
import com.hackaboss.travelagency.repository.HotelRepository;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HotelBookingService implements IHotelBookingService {

    private final HotelBookingMapper hotelBookingMapper;
    private final HotelBookingRepository hotelBookingRepository;
    private final IHotelService hotelService;
    //private final IUserService userService;

    public HotelBookingService(HotelBookingMapper hotelBookingMapper, HotelBookingRepository hotelBookingRepository, IHotelService hotelService) {
        this.hotelBookingMapper = hotelBookingMapper;
        this.hotelBookingRepository = hotelBookingRepository;
        this.hotelService = hotelService;
    }




    @Override
    public List<HotelBookingDTOResponse> findAll() {
        return hotelBookingRepository.findAll().stream()
                .map(hotelBookingMapper::entityToDTO)
                .toList();
    }

    @Override
    public String createHotelBooking(HotelBookingDTORequest dto) {
        HotelBooking booking = hotelBookingMapper.requestToEntity(dto);

        // Buscar hotel por hotelCode (igual que antes)
        /*
        Hotel hotel = hotelService.findByHotelCode(dto.getHotel().getHotelCode())
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado"));
        booking.setHotel(hotel);*/

        // Buscar el user principal
        /*
        if (dto.getUser() != null && dto.getUser().getId() != null) {
            User userPrincipal = userService.findById(dto.getUser().getId())
                    .orElseThrow(() -> new RuntimeException("Usuario principal no encontrado"));
            booking.setUser(userPrincipal);
        }*/

        // Guardar
        hotelBookingRepository.save(booking);
        return "Reserva creada con éxito. ID: " + booking.getId();
    }



}
