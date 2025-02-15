package com.hackaboss.travelagency.controller;

import com.hackaboss.travelagency.dto.request.HotelDTORequest;
import com.hackaboss.travelagency.dto.response.HotelDTOResponse;
import com.hackaboss.travelagency.service.HotelService;
import com.hackaboss.travelagency.service.IHotelService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agency")
public class HotelController {

    private final IHotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    // GET /agency/hotels - Obtiene el listado de todos los hoteles registrados
    @GetMapping("/hotels")
    public ResponseEntity<List<HotelDTOResponse>> getAllHotels() {
        List<HotelDTOResponse> hotels = hotelService.findAll();
        return ResponseEntity.ok(hotels);
    }

    // POST /agency/hotels - Crea un nuevo hotel
    @PostMapping("/hotels")
    public ResponseEntity<String> createHotel(@Valid @RequestBody HotelDTORequest hotelDTORequest) {
        String createdHotel = hotelService.createHotel(hotelDTORequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdHotel);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<HotelDTOResponse>> getAvailableRooms(
            @RequestParam("dateFrom") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateFrom,
            @RequestParam("dateTo") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateTo,
            @RequestParam("destination") String destination) {
        List<HotelDTOResponse> availableRooms = hotelService.findAvailableRooms(destination, dateFrom, dateTo );
        return ResponseEntity.ok(availableRooms);
    }
}