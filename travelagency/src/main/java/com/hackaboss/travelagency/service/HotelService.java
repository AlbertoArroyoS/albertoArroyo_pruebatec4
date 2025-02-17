package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.HotelDTORequest;
import com.hackaboss.travelagency.dto.response.HotelDTOResponse;
import com.hackaboss.travelagency.exception.EntityNotFoundException;
import com.hackaboss.travelagency.exception.InvalidDataException;
import com.hackaboss.travelagency.mapper.HotelMapper;
import com.hackaboss.travelagency.model.Hotel;
import com.hackaboss.travelagency.repository.HotelBookingRepository;
import com.hackaboss.travelagency.repository.HotelRepository;
import com.hackaboss.travelagency.util.Booked;
import com.hackaboss.travelagency.util.RoomType;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class HotelService implements IHotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final HotelBookingRepository hotelBookingRepository;

    public HotelService(HotelRepository hotelRepository, HotelMapper hotelMapper, HotelBookingRepository hotelBookingRepository) {
        this.hotelRepository = hotelRepository;
        this.hotelMapper = hotelMapper;
        this.hotelBookingRepository = hotelBookingRepository;
    }


    @Override
    public List<HotelDTOResponse> findAll() {
        return hotelRepository.findByActiveTrue().stream()
                .map(hotelMapper::entityToDTO)
                .toList();
    }

    @Override
    public String createHotel(HotelDTORequest hotelDTORequest) {
        if (hotelDTORequest == null) {
            throw new InvalidDataException("Datos del hotel no pueden ser nulos. No se pudo crear.");
        }
        Hotel hotel = hotelMapper.requestToEntity(hotelDTORequest);
        hotelRepository.save(hotel);
        return "Hotel creado con éxito";
    }

    @Override
    public Optional<HotelDTOResponse> findById(Long id) {
        if (id == null) {
            throw new InvalidDataException("El campo ID no puede ser nulo. No se pudo buscar.");
        }
        return hotelRepository.findByIdAndActiveTrue(id)
                .map(hotelMapper::entityToDTO);
    }

    @Override
    public List<HotelDTOResponse> findAvailableRooms(String destination, LocalDate requestDateFrom, LocalDate requestDateTo) {
        if (destination == null || requestDateFrom == null || requestDateTo == null || requestDateFrom.isAfter(requestDateTo)) {
            throw new InvalidDataException("Parámetros inválidos: verifique destino y rango de fechas.");
        }

        List<Hotel> availableHotels = hotelRepository.findByCityAndBookedAndDateFromLessThanEqualAndDateToGreaterThanEqualAndActiveTrue(
                destination,
                Booked.NO,
                requestDateFrom,
                requestDateTo
        );

        return availableHotels.stream()
                .map(hotelMapper::entityToDTO)
                .toList();
    }

    @Override
    @Transactional
    public String updateHotel(Long id, HotelDTORequest hotelDTORequest) {
        if (id == null || hotelDTORequest == null) {
            throw new InvalidDataException("Datos inválidos para actualizar el hotel. No se pudo actualizar.");
        }
        Hotel hotel = hotelRepository.findByIdAndActiveTrue(id)
                .orElse(null);

        if (hotel == null) {
            throw new EntityNotFoundException("No se encontró el hotel con ID " + id + ". No se pudo actualizar.");
        }

        hotel.setHotelCode(hotelDTORequest.getHotelCode());
        hotel.setName(hotelDTORequest.getName());
        hotel.setCity(hotelDTORequest.getCity());
        hotel.setRoomType(RoomType.valueOf(hotelDTORequest.getRoomType()));
        hotel.setRatePerNight(hotelDTORequest.getRatePerNight());
        hotel.setDateFrom(hotelDTORequest.getDateFrom());
        hotel.setDateTo(hotelDTORequest.getDateTo());

        hotelRepository.save(hotel);
        return "Hotel actualizado con éxito";
    }

    @Override
    @Transactional
    public String deleteHotel(Long id) {
        if (id == null) {
            throw new InvalidDataException("ID del hotel no puede ser nulo. No se pudo eliminar.");
        }
        Hotel hotel = hotelRepository.findByIdAndActiveTrue(id)
                .orElse(null);

        if (hotel == null) {
            throw new EntityNotFoundException("No se encontró el hotel con ID " + id + ". No se pudo eliminar.");
        }

        // Verificar si el hotel tiene reservas activas
        boolean hasBookings = hotelBookingRepository.existsByHotelAndActiveTrue(hotel);
        if (hasBookings) {
            return "No se puede eliminar el hotel, tiene reservas activas.";
        }

        hotel.setActive(false);
        hotelRepository.save(hotel);
        return "Hotel eliminado con éxito";
    }
}