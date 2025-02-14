package com.hackaboss.travelagency.controller;

import com.hackaboss.travelagency.dto.response.HotelBookingDTOResponse;
import com.hackaboss.travelagency.service.IHotelBookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/agency/room-booking")
public class HotelBookingController {

    private final IHotelBookingService hotelBookingService;

    public HotelBookingController(IHotelBookingService hotelBookingService) {
        this.hotelBookingService = hotelBookingService;
    }


    // GET /agency/flights - Obtiene el listado de todos los vuelos registrados
    @GetMapping
    public ResponseEntity<List<HotelBookingDTOResponse>> getAllHotelBookings() {
        List<HotelBookingDTOResponse> flights = hotelBookingService.findAll();
        return ResponseEntity.ok(flights);
    }
}
