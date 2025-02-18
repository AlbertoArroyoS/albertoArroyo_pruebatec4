package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.HotelBookingDTORequest;
import com.hackaboss.travelagency.dto.request.UserDTORequest;
import com.hackaboss.travelagency.dto.response.HotelBookingDTOResponse;
import com.hackaboss.travelagency.exception.EntityNotDeletableException;
import com.hackaboss.travelagency.exception.EntityNotFoundException;
import com.hackaboss.travelagency.exception.InvalidDataException;
import com.hackaboss.travelagency.mapper.HotelBookingMapper;
import com.hackaboss.travelagency.model.Hotel;
import com.hackaboss.travelagency.model.HotelBooking;
import com.hackaboss.travelagency.model.User;
import com.hackaboss.travelagency.repository.HotelBookingRepository;
import com.hackaboss.travelagency.util.Booked;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class HotelBookingService implements IHotelBookingService {

    private final HotelBookingMapper hotelBookingMapper;
    private final HotelBookingRepository hotelBookingRepository;
    private final IHotelService hotelService;
    private final IUserService userService;

    public HotelBookingService(HotelBookingMapper hotelBookingMapper, HotelBookingRepository hotelBookingRepository, IHotelService hotelService, IUserService userService) {
        this.hotelBookingMapper = hotelBookingMapper;
        this.hotelBookingRepository = hotelBookingRepository;
        this.hotelService = hotelService;
        this.userService = userService;
    }


    @Override
    @Transactional
    public List<HotelBookingDTOResponse> findAll() {
        return hotelBookingRepository.findByActiveTrue().stream()
                .map(hotelBookingMapper::entityToDTO)
                .toList();
    }


    @Override
    @Transactional
    public String createHotelBooking(HotelBookingDTORequest dto) {
        // Validar datos
        if (dto == null || dto.getHotel() == null) {
            throw new InvalidDataException("Los datos de la reserva no pueden ser nulos.");
        }
        //Comprobar si el hotel existe
        Hotel hotelEntity = hotelService.findActiveHotelByCode(dto.getHotel().getHotelCode());
        if (hotelEntity == null) {
            throw new EntityNotFoundException("No existe el hotel con código: " + dto.getHotel().getHotelCode());
        }

        //Comprobar si existe por existsByHotelAndDateFromAndDateToAndActiveTrue
        boolean existHotel = hotelBookingRepository.existsByHotelAndDateFromAndDateToAndActiveTrue(hotelEntity, dto.getHotel().getDateFrom(), dto.getHotel().getDateTo());
        if (existHotel) {
            throw new EntityNotDeletableException("Ya existe una reserva activa para este hotel en las mismas fechas.");
        }

        //Comprobar si ya existe una reserva activa para el hotel
        Integer resultHotelId = hotelBookingRepository.countByHotelIdNative(hotelEntity.getId());
        boolean existHotelId = resultHotelId != null && resultHotelId > 0;
        if (existHotelId) {
            throw new EntityNotDeletableException("Ya existe una reserva activa para este hotel.");
        }

        List<User> hostEntities = new ArrayList<>();
        for (UserDTORequest hostDTO : dto.getHosts()) {
            User host = userService.findOrCreateUserByDni(hostDTO);
            hostEntities.add(host);
        }

        HotelBooking booking = hotelBookingMapper.requestToEntity(dto);
        booking.setHotel(hotelEntity);
        booking.setHosts(hostEntities);
        booking = hotelBookingRepository.save(booking);

        hotelEntity.setBooked(Booked.SI);
        hotelService.save(hotelEntity);

        return "Reserva creada correctamente con ID: " + booking.getId();
    }

}
