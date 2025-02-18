package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.HotelBookingDTORequest;
import com.hackaboss.travelagency.dto.request.UserDTORequest;
import com.hackaboss.travelagency.dto.response.HotelBookingDTOResponse;
import com.hackaboss.travelagency.dto.response.UserDTOResponse;
import com.hackaboss.travelagency.exception.EntityNotDeletableException;
import com.hackaboss.travelagency.exception.EntityNotFoundException;
import com.hackaboss.travelagency.exception.InvalidDataException;
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

    private final HotelBookingRepository hotelBookingRepository;
    private final IHotelService hotelService;
    private final IUserService userService;

    public HotelBookingService(HotelBookingRepository hotelBookingRepository, IHotelService hotelService, IUserService userService) {
        this.hotelBookingRepository = hotelBookingRepository;
        this.hotelService = hotelService;
        this.userService = userService;
    }

    @Override
    @Transactional
    public List<HotelBookingDTOResponse> findAll() {
        return hotelBookingRepository.findByActiveTrue().stream()
                .map(this::entityToDTO)
                .toList();
    }

    @Override
    @Transactional
    public String createHotelBooking(HotelBookingDTORequest dto) {
        if (dto == null || dto.getHotel() == null) {
            throw new InvalidDataException("Los datos de la reserva no pueden ser nulos.");
        }

        Hotel hotelEntity = hotelService.findActiveHotelByCode(dto.getHotel().getHotelCode());
        if (hotelEntity == null) {
            throw new EntityNotFoundException("No existe el hotel con código: " + dto.getHotel().getHotelCode());
        }

        boolean existHotel = hotelBookingRepository.existsByHotelAndDateFromAndDateToAndActiveTrue(hotelEntity, dto.getHotel().getDateFrom(), dto.getHotel().getDateTo());
        if (existHotel) {
            throw new EntityNotDeletableException("Ya existe una reserva activa para este hotel en las mismas fechas.");
        }

        Integer resultHotelId = hotelBookingRepository.countByHotelIdNative(hotelEntity.getId());
        boolean existHotelId = resultHotelId != null && resultHotelId > 0;
        if (existHotelId) {
            throw new EntityNotDeletableException("Ya existe una reserva activa para este hotel.");
        }

        List<User> hostEntities = new ArrayList<>();
        for (UserDTORequest hostDTO : dto.getHosts()) {
            if (hostDTO.getDni() == null || hostDTO.getDni().isBlank()) {
                throw new InvalidDataException("El DNI del huésped no puede ser nulo o vacío");
            }
            User host = userService.findOrCreateUserByDni(hostDTO);
            hostEntities.add(host);
        }

        HotelBooking booking = requestToEntity(dto);
        booking.setHotel(hotelEntity);
        booking.setHosts(hostEntities);
        booking = hotelBookingRepository.save(booking);

        hotelEntity.setBooked(Booked.SI);
        hotelService.save(hotelEntity);

        return "Reserva creada correctamente con ID: " + booking.getId();
    }

    // Métodos de conversión
    private HotelBooking requestToEntity(HotelBookingDTORequest request) {
        return HotelBooking.builder()
                .id(request.getId())
                .hotel(hotelService.findActiveHotelByCode(request.getHotel().getHotelCode()))
                .build();
    }

    private HotelBookingDTOResponse entityToDTO(HotelBooking booking) {
        return HotelBookingDTOResponse.builder()
                .dateFrom(booking.getHotel().getDateFrom())
                .dateTo(booking.getHotel().getDateTo())
                .nights(booking.getHotel().getDateFrom().until(booking.getHotel().getDateTo()).getDays())
                .city(booking.getHotel().getCity())
                .hotelCode(booking.getHotel().getHotelCode())
                .peopleQuantity(booking.getHosts().size())
                .roomType(booking.getHotel().getRoomType().toString())
                .price(booking.getHotel().getRatePerNight() * booking.getHosts().size())
                .hosts(booking.getHosts().stream().map(this::userToDTO).toList())
                .build();
    }

    private UserDTOResponse userToDTO(User user) {
        return UserDTOResponse.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .phone(user.getPhone())
                .dni(user.getDni())
                .build();
    }
}

