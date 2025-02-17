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
import com.hackaboss.travelagency.repository.HotelRepository;
import com.hackaboss.travelagency.repository.UserRepository;
import com.hackaboss.travelagency.util.Booked;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class HotelBookingService implements IHotelBookingService {

    private final HotelBookingMapper hotelBookingMapper;
    private final HotelBookingRepository hotelBookingRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;


    public HotelBookingService(HotelBookingMapper hotelBookingMapper, HotelBookingRepository hotelBookingRepository, HotelRepository hotelRepository, UserRepository userRepository) {
        this.hotelBookingMapper = hotelBookingMapper;
        this.hotelBookingRepository = hotelBookingRepository;
        this.hotelRepository = hotelRepository;
        this.userRepository = userRepository;
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
        if (dto == null || dto.getHotel() == null) {
            throw new InvalidDataException("Los datos de la reserva no pueden ser nulos.");
        }

        // Buscar el hotel
        Hotel hotelEntity = (dto.getHotel().getId() != null) ?
                hotelRepository.findByIdAndActiveTrue(dto.getHotel().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Hotel no encontrado")) :
                hotelRepository.findByHotelCodeAndActiveTrue(dto.getHotel().getHotelCode())
                        .orElseThrow(() -> new EntityNotFoundException("Hotel no encontrado"));

        // Verificar si ya existe una reserva en las mismas fechas
        boolean existsBooking = hotelBookingRepository.existsByHotelAndDateFromAndDateToAndActiveTrue(
                hotelEntity, dto.getHotel().getDateFrom(), dto.getHotel().getDateTo());
        if (existsBooking) {
            throw new EntityNotDeletableException("Ya existe una reserva activa para este hotel en las mismas fechas.");
        }

        // Procesar huéspedes y guardar reserva
        List<User> hostEntities = new ArrayList<>();
        for (UserDTORequest hostDTO : dto.getHosts()) {
            User host = userRepository.findByDni(hostDTO.getDni())
                    .orElseGet(() -> {
                        User newUser = User.builder()
                                .name(hostDTO.getName())
                                .surname(hostDTO.getSurname())
                                .phone(hostDTO.getPhone())
                                .dni(hostDTO.getDni())
                                .username(hostDTO.getDni())
                                .build();
                        return userRepository.save(newUser);
                    });
            hostEntities.add(host);
        }

        // Guardar la reserva
        HotelBooking booking = hotelBookingMapper.requestToEntity(dto);
        booking.setHotel(hotelEntity);
        booking.setHosts(hostEntities);
        booking = hotelBookingRepository.save(booking);

        // Actualizar estado del hotel
        hotelEntity.setBooked(Booked.SI);
        hotelRepository.save(hotelEntity);

        return "Reserva creada correctamente con ID: " + booking.getId();
    }


}
