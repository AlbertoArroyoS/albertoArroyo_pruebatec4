package com.hackaboss.travelagency.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.time.LocalDate;

import com.hackaboss.travelagency.dto.request.HotelDTORequest;
import com.hackaboss.travelagency.exception.InvalidDataException;
import com.hackaboss.travelagency.mapper.HotelMapper;
import com.hackaboss.travelagency.model.Hotel;
import com.hackaboss.travelagency.repository.HotelRepository;
import com.hackaboss.travelagency.util.RoomType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @InjectMocks
    private HotelService hotelService; // Servicio a probar

    @Mock
    private HotelRepository hotelRepository; // Mock para el repositorio

    @Mock
    private HotelMapper hotelMapper; // Mock para el mapper

    @Test
    void createHotelSuccessTest() {
        // Dado un HotelDTORequest válido
        HotelDTORequest dto = new HotelDTORequest();
        dto.setHotelCode("HTL-59999");
        dto.setName("Hotel Sunrise");
        dto.setCity("Barcelona");
        dto.setRoomType("SUITE");
        dto.setRatePerNight(200.0);
        dto.setDateFrom(LocalDate.of(2024, 7, 1));
        dto.setDateTo(LocalDate.of(2024, 7, 20));

        // Creamos la entidad Hotel que se espera recibir
        Hotel hotelEntity = new Hotel();
        hotelEntity.setHotelCode(dto.getHotelCode());
        hotelEntity.setName(dto.getName());
        hotelEntity.setCity(dto.getCity());
        hotelEntity.setRoomType(RoomType.valueOf(dto.getRoomType()));
        hotelEntity.setRatePerNight(dto.getRatePerNight());
        hotelEntity.setDateFrom(dto.getDateFrom());
        hotelEntity.setDateTo(dto.getDateTo());

        // Configuramos el comportamiento de los mocks usando doReturn()
        doReturn(hotelEntity).when(hotelMapper).requestToEntity(dto);
        doReturn(hotelEntity).when(hotelRepository).save(any(Hotel.class));

        // Cuando se crea el hotel
        String response = hotelService.createHotel(dto);

        // Entonces se debe recibir el mensaje de éxito
        Assertions.assertEquals("Hotel creado con éxito", response);
        verify(hotelMapper, times(1)).requestToEntity(dto);
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void createHotelNullDtoThrowsExceptionTest() {
        // Dado un DTO nulo, se espera lanzar una InvalidDataException
        Assertions.assertThrows(InvalidDataException.class, () -> {
            hotelService.createHotel(null);
        });
    }
}
