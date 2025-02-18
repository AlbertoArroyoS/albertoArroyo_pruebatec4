package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.HotelDTORequest;
import com.hackaboss.travelagency.dto.response.HotelDTOResponse;
import com.hackaboss.travelagency.exception.EntityExistsException;
import com.hackaboss.travelagency.exception.EntityNotFoundException;
import com.hackaboss.travelagency.exception.InvalidDataException;
import com.hackaboss.travelagency.model.Hotel;
import com.hackaboss.travelagency.repository.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelService hotelService;

    @BeforeEach
    public void setup() {
        reset(hotelRepository);
    }

    @Test
    @DisplayName("Test para listar todos los hoteles activos cuando la lista está vacía")
    void testFindAllEmpty() {
        when(hotelRepository.findByActiveTrue()).thenReturn(Collections.emptyList());
        assertThrows(EntityNotFoundException.class, () -> hotelService.findAll());
    }

    @Test
    @DisplayName("Test para crear un hotel con código y nombre ya existentes")
    void testCreateHotelExisting() {
        HotelDTORequest dtoRequest = new HotelDTORequest();
        dtoRequest.setHotelCode("H001");
        dtoRequest.setName("Hotel Test");

        Hotel hotel = new Hotel();
        hotel.setHotelCode("H001");
        hotel.setName("Hotel Test");

        when(hotelRepository.findByHotelCodeAndName("H001", "Hotel Test")).thenReturn(Optional.of(hotel));
        assertThrows(EntityExistsException.class, () -> hotelService.createHotel(dtoRequest));
    }

    @Test
    @DisplayName("Test para buscar un hotel por ID con ID nulo")
    void testFindByIdInvalid() {
        assertThrows(InvalidDataException.class, () -> hotelService.findById(null));
    }

    @Test
    @DisplayName("Test para buscar un hotel por ID que no existe")
    void testFindByIdNotFound() {
        Long id = 1L;
        when(hotelRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.empty());
        Optional<HotelDTOResponse> result = hotelService.findById(id);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test para eliminar un hotel sin reservas activas")
    void testDeleteHotel() {
        Long id = 1L;
        Hotel hotel = new Hotel();
        hotel.setId(id);
        hotel.setActive(true);
        hotel.setListHotelBookings(Collections.emptyList());

        when(hotelRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(hotel)).thenReturn(hotel);

        String result = hotelService.deleteHotel(id);
        assertEquals("Hotel eliminado con éxito", result);
    }

    @Test
    @DisplayName("Test para eliminar un hotel que no existe")
    void testDeleteHotelNotFound() {
        Long id = 1L;
        when(hotelRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> hotelService.deleteHotel(id));
    }
}
