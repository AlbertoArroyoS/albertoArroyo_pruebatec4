package com.hackaboss.travelagency.service;

import java.time.LocalDate;
import com.hackaboss.travelagency.dto.request.HotelDTORequest;
import com.hackaboss.travelagency.dto.response.HotelDTOResponse;
import com.hackaboss.travelagency.exception.EntityExistsException;
import com.hackaboss.travelagency.exception.EntityNotFoundException;
import com.hackaboss.travelagency.exception.InvalidDataException;
import com.hackaboss.travelagency.mapper.HotelMapper;
import com.hackaboss.travelagency.model.Hotel;
import com.hackaboss.travelagency.model.HotelBooking;
import com.hackaboss.travelagency.repository.HotelRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelService hotelService;

    @BeforeEach
    public void setup() {
        // Con @ExtendWith(MockitoExtension.class) los mocks se inicializan automáticamente
        reset(hotelRepository, hotelMapper);
    }

    @Test
    @DisplayName("Test para listar todos los hoteles activos")
    void testFindAll() {
        Hotel hotel1 = new Hotel();
        Hotel hotel2 = new Hotel();
        List<Hotel> hotels = List.of(hotel1, hotel2);

        // Simulamos la conversión de entidad a DTO
        HotelDTOResponse dto1 = new HotelDTOResponse();
        HotelDTOResponse dto2 = new HotelDTOResponse();

        when(hotelRepository.findByActiveTrue()).thenReturn(hotels);
        when(hotelMapper.entityToDTO(hotel1)).thenReturn(dto1);
        when(hotelMapper.entityToDTO(hotel2)).thenReturn(dto2);

        List<HotelDTOResponse> result = hotelService.findAll();
        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Test para listar todos los hoteles activos cuando la lista está vacía")
    void testFindAllEmpty() {
        when(hotelRepository.findByActiveTrue()).thenReturn(Collections.emptyList());
        // Dado que la lista mapeada quedará vacía, se lanzará excepción
        assertThrows(EntityNotFoundException.class, () -> hotelService.findAll());
    }

    @Test
    @DisplayName("Test para crear un nuevo hotel")
    void testCreateHotel() {
        HotelDTORequest dtoRequest = new HotelDTORequest();
        dtoRequest.setHotelCode("H001");
        dtoRequest.setName("Hotel Test");
        // Otros campos necesarios se pueden completar según la implementación

        Hotel hotel = new Hotel();
        hotel.setHotelCode("H001");
        hotel.setName("Hotel Test");

        when(hotelRepository.findByHotelCodeAndName("H001", "Hotel Test")).thenReturn(Optional.empty());
        when(hotelMapper.requestToEntity(dtoRequest)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenReturn(hotel);

        String result = hotelService.createHotel(dtoRequest);
        assertEquals("Hotel creado con éxito", result);
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
    @DisplayName("Test para buscar un hotel por ID")
    void testFindById() {
        Long id = 1L;
        Hotel hotel = new Hotel();
        hotel.setId(id);
        HotelDTOResponse dtoResponse = new HotelDTOResponse();

        when(hotelRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.of(hotel));
        when(hotelMapper.entityToDTO(hotel)).thenReturn(dtoResponse);

        Optional<HotelDTOResponse> result = hotelService.findById(id);
        assertTrue(result.isPresent());
        assertEquals(dtoResponse, result.get());
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
    @DisplayName("Test para buscar habitaciones disponibles con parámetros inválidos")
    void testFindAvailableRoomsInvalidParams() {
        String destination = "Madrid";
        // Fecha de inicio posterior a fecha de fin
        LocalDate dateFrom = LocalDate.of(2025, 5, 10);
        LocalDate dateTo = LocalDate.of(2025, 5, 1);

        assertThrows(InvalidDataException.class, () ->
                hotelService.findAvailableRooms(destination, dateFrom, dateTo));
    }

    @Test
    @DisplayName("Test para actualizar un hotel que no existe")
    void testUpdateHotelNotFound() {
        Long id = 1L;
        HotelDTORequest dtoRequest = new HotelDTORequest();

        when(hotelRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> hotelService.updateHotel(id, dtoRequest));
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


    @Test
    @DisplayName("Test para buscar hotel activo por código")
    void testFindActiveHotelByCode() {
        String hotelCode = "H001";
        Hotel hotel = new Hotel();
        hotel.setHotelCode(hotelCode);

        when(hotelRepository.findByHotelCodeAndActiveTrue(hotelCode)).thenReturn(Optional.of(hotel));
        Hotel result = hotelService.findActiveHotelByCode(hotelCode);
        assertNotNull(result);
        assertEquals(hotelCode, result.getHotelCode());
    }

    @Test
    @DisplayName("Test para buscar hotel activo por código cuando no existe")
    void testFindActiveHotelByCodeNotFound() {
        String hotelCode = "H001";
        when(hotelRepository.findByHotelCodeAndActiveTrue(hotelCode)).thenReturn(Optional.empty());
        Hotel result = hotelService.findActiveHotelByCode(hotelCode);
        assertNull(result);
    }

    @Test
    @DisplayName("Test para guardar un hotel")
    void testSave() {
        Hotel hotel = new Hotel();
        when(hotelRepository.save(hotel)).thenReturn(hotel);
        Hotel result = hotelService.save(hotel);
        assertEquals(hotel, result);
    }
}
