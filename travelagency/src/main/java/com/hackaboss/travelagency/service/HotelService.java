package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.HotelDTORequest;
import com.hackaboss.travelagency.dto.response.HotelDTOResponse;
import com.hackaboss.travelagency.mapper.HotelMapper;
import com.hackaboss.travelagency.model.Hotel;
import com.hackaboss.travelagency.repository.HotelRepository;
import com.hackaboss.travelagency.util.Booked;
import com.hackaboss.travelagency.util.RoomType;
import jakarta.persistence.EntityNotFoundException;
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

    @Autowired
    public HotelService(HotelRepository hotelRepository, HotelMapper hotelMapper) {
        this.hotelRepository = hotelRepository;
        this.hotelMapper = hotelMapper;
    }


    @Override
    public List<HotelDTOResponse> findAll() {
        return hotelRepository.findByActiveTrue().stream()
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

    @Override
    public Optional<HotelDTOResponse> findById(Long id) {
        return hotelRepository.findByIdAndActiveTrue(id)
                .map(hotelMapper::entityToDTO);
    }

    @Override
    public List<HotelDTOResponse> findAvailableRooms(String destination, LocalDate requestDateFrom, LocalDate requestDateTo) {
        // Validación de parámetros
        if (destination == null || requestDateFrom == null || requestDateTo == null || requestDateFrom.isAfter(requestDateTo)) {
            throw new IllegalArgumentException("Parámetros inválidos: verifique destino y rango de fechas.");
        }

        // Se consulta el repositorio para obtener los hoteles que:
        // - Coinciden con el destino (ciudad)
        // - No están reservados (Booked.NO)
        // - Tienen un rango de disponibilidad que cubre el rango solicitado
        List<Hotel> availableHotels = hotelRepository.findByCityAndBookedAndDateFromLessThanEqualAndDateToGreaterThanEqualAndActiveTrue(
                destination,
                Booked.NO,
                requestDateFrom,
                requestDateTo
        );

        // Se mapea cada entidad Hotel a HotelDTOResponse utilizando el mapper configurado
        return availableHotels.stream()
                .map(hotel -> hotelMapper.entityToDTO(hotel))
                .toList();
    }

    @Override
    @Transactional
    public String updateHotel(Long id, HotelDTORequest hotelDTORequest) {
        Hotel hotel = hotelRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel no encontrado"));

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
        Hotel hotel = hotelRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Hotel no encontrado"));
        hotel.setActive(false);
        hotelRepository.save(hotel);
        return "Hotel eliminado con éxito";
    }

}

