package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.HotelBookingDTORequest;
import com.hackaboss.travelagency.dto.request.UserDTORequest;
import com.hackaboss.travelagency.dto.response.HotelBookingDTOResponse;
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
import java.util.Optional;
import java.util.stream.Collectors;

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
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String createHotelBooking(HotelBookingDTORequest dto) {
        // Buscar el hotel: se busca por id (si se envía) o por hotelCode
        Hotel hotelEntity;
        if (dto.getHotel().getId() != null) {
            Optional<Hotel> hotelOpt = hotelRepository.findByIdAndActiveTrue(dto.getHotel().getId());
            if (hotelOpt.isEmpty()) {
                return "Hotel no encontrado";
            }
            hotelEntity = hotelOpt.get();
        } else {
            Optional<Hotel> hotelOpt = hotelRepository.findByHotelCodeAndActiveTrue(dto.getHotel().getHotelCode());
            if (hotelOpt.isEmpty()) {
                return "Hotel no encontrado";
            }
            hotelEntity = hotelOpt.get();
        }

        // Validar si ya existe una reserva para este hotel en las mismas fechas
        boolean existsBooking = hotelBookingRepository.existsByHotelAndDateFromAndDateToAndActiveTrue(
                hotelEntity, dto.getHotel().getDateFrom(), dto.getHotel().getDateTo());
        if (existsBooking) {
            return "Ya existe una reserva activa para este hotel en las mismas fechas.";
        }

        // Procesar los hosts: buscar por DNI y crear si no existen
        List<User> hostEntities = new ArrayList<>();
        for (UserDTORequest hostDTO : dto.getHosts()) {
            Optional<User> userOpt = userRepository.findByDni(hostDTO.getDni());
            if (userOpt.isPresent()) {
                hostEntities.add(userOpt.get());
            } else {
                User newUser = User.builder()
                        .name(hostDTO.getName())
                        .surname(hostDTO.getSurname())
                        .phone(hostDTO.getPhone())
                        .dni(hostDTO.getDni())
                        // Asegúrate de asignar el username si es obligatorio
                        .username(hostDTO.getDni()) // o la lógica que corresponda
                        .build();
                newUser = userRepository.save(newUser);
                hostEntities.add(newUser);
            }
        }

        // Mapear el resto de la reserva y asignar el hotel y la lista completa de hosts
        HotelBooking booking = hotelBookingMapper.requestToEntity(dto);
        booking.setHotel(hotelEntity);
        booking.setHosts(hostEntities);  // Se asigna la lista completa de hosts

        // Guardar la reserva y retornar mensaje
        booking = hotelBookingRepository.save(booking);

        // Actualizar el estado del hotel a "SI" en el campo booked
        hotelEntity.setBooked(Booked.SI);
        hotelRepository.save(hotelEntity);

        return "Reserva creada correctamente con ID: " + booking.getId();
    }





}
