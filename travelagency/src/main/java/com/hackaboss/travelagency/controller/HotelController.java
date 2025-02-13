package com.hackaboss.travelagency.controller;

import com.hackaboss.travelagency.dto.request.HotelDTORequest;
import com.hackaboss.travelagency.dto.response.HotelDTOResponse;
import com.hackaboss.travelagency.service.HotelService;
import com.hackaboss.travelagency.service.IHotelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agency/hotels")
public class HotelController {

    private final IHotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    // GET /agency/hotels - Obtiene el listado de todos los hoteles registrados
    @GetMapping
    public ResponseEntity<List<HotelDTOResponse>> getAllHotels() {
        List<HotelDTOResponse> hotels = hotelService.findAll();
        return ResponseEntity.ok(hotels);
    }

    // POST /agency/hotels - Crea un nuevo hotel
    @PostMapping
    public ResponseEntity<String> createHotel(@RequestBody HotelDTORequest hotelDTORequest) {
        String createdHotel = hotelService.createHotel(hotelDTORequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdHotel);
    }
}