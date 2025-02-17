package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.HotelDTORequest;
import com.hackaboss.travelagency.exception.InvalidDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class HotelServiceTest {

    @Autowired
    private HotelService hotelService;

    @Test
    void createHotelSuccessTest() {
        // Dado un HotelDTORequest válido
        HotelDTORequest dto = new HotelDTORequest();
        dto.setHotelCode("HTL-54321");
        dto.setName("Hotel Sunrise");
        dto.setCity("Barcelona");
        dto.setRoomType("SUITE");
        dto.setRatePerNight(200.0);
        dto.setDateFrom(LocalDate.of(2024, 7, 1));
        dto.setDateTo(LocalDate.of(2024, 7, 20));

        // Cuando se crea el hotel
        String response = hotelService.createHotel(dto);

        // Entonces se debe recibir el mensaje de éxito
        Assertions.assertEquals("Hotel creado con éxito", response);
    }

    @Test
    void createHotelNullDtoThrowsExceptionTest() {
        // Dado un DTO nulo, se espera lanzar una InvalidDataException
        Assertions.assertThrows(InvalidDataException.class, () -> {
            hotelService.createHotel(null);
        });
    }
}

