package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.HotelBookingDTORequest;
import com.hackaboss.travelagency.dto.response.HotelBookingDTOResponse;
import com.hackaboss.travelagency.mapper.HotelBookingMapper;
import com.hackaboss.travelagency.model.Hotel;
import com.hackaboss.travelagency.model.HotelBooking;
import com.hackaboss.travelagency.repository.HotelBookingRepository;
import com.hackaboss.travelagency.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HotelBookingService implements IHotelBookingService {

    private final HotelBookingMapper bookingMapper;
    private final HotelRepository hotelRepository;
    private final HotelBookingRepository bookingRepository;

    public HotelBookingService(HotelBookingMapper bookingMapper,
                               HotelRepository hotelRepository,
                               HotelBookingRepository bookingRepository) {
        this.bookingMapper = bookingMapper;
        this.hotelRepository = hotelRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public List<HotelBookingDTOResponse> findAll() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::entityToDTO)
                .toList();
    }

    @Override
    public String createHotelBooking(HotelBookingDTORequest dto) {
        // 1. Convertir DTO a entidad
        HotelBooking booking = bookingMapper.requestToEntity(dto);

        // 2. Buscar el hotel
        Hotel hotel = hotelRepository.findByHotelCode(dto.getHotelCode())
                .orElseThrow(() -> new RuntimeException("Hotel no encontrado"));
        booking.setHotel(hotel);

        // 3. Guardar la reserva
        HotelBooking saved = bookingRepository.save(booking);

        // 4. Devolver un mensaje con el ID, por ejemplo
        return "Reserva creada con éxito. ID: " + saved.getId();
    }

}
