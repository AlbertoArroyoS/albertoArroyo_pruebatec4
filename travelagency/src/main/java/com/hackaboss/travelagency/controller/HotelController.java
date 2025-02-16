package com.hackaboss.travelagency.controller;

import com.hackaboss.travelagency.dto.request.HotelDTORequest;
import com.hackaboss.travelagency.dto.response.HotelDTOResponse;
import com.hackaboss.travelagency.service.HotelService;
import com.hackaboss.travelagency.service.IHotelService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/agency/hotels")
public class HotelController {

    private final IHotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public ResponseEntity<List<HotelDTOResponse>> getAllHotels() {
        List<HotelDTOResponse> hotels = hotelService.findAll();
        return ResponseEntity.ok(hotels);
    }

    @PostMapping("/new")
    public ResponseEntity<String> createHotel(@Valid @RequestBody HotelDTORequest hotelDTORequest) {
        String response = hotelService.createHotel(hotelDTORequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updateHotel(@PathVariable Long id, @Valid @RequestBody HotelDTORequest hotelDTORequest) {
        String response = hotelService.updateHotel(id, hotelDTORequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteHotel(@PathVariable Long id) {
        String response = hotelService.deleteHotel(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDTOResponse> getHotelById(@PathVariable Long id) {
        return hotelService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Hotel no encontrado"));
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<HotelDTOResponse>> getAvailableRooms(
            @RequestParam("dateFrom") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateFrom,
            @RequestParam("dateTo") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateTo,
            @RequestParam("destination") String destination) {
        List<HotelDTOResponse> availableRooms = hotelService.findAvailableRooms(destination, dateFrom, dateTo);
        return ResponseEntity.ok(availableRooms);
    }
}