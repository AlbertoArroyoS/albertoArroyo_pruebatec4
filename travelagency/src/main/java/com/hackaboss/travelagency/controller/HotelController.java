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
import java.util.Optional;

@RestController
@RequestMapping("/agency/hotels")
public class HotelController {

    private final IHotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public ResponseEntity<List<HotelDTOResponse>> getAllHotels() {
        try {
            return ResponseEntity.ok(hotelService.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<String> createHotel(@Valid @RequestBody HotelDTORequest hotelDTORequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.createHotel(hotelDTORequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updateHotel(@PathVariable Long id, @Valid @RequestBody HotelDTORequest hotelDTORequest) {
        try {
            return ResponseEntity.ok(hotelService.updateHotel(id, hotelDTORequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteHotel(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(hotelService.deleteHotel(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHotelById(@PathVariable Long id) {
        Optional<HotelDTOResponse> hotelOpt = hotelService.findById(id);
        if (hotelOpt.isPresent()) {
            return ResponseEntity.ok(hotelOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró hotel con ID: " + id);
        }
    }


    @GetMapping("/rooms")
    public ResponseEntity<List<HotelDTOResponse>> getAvailableRooms(
            @RequestParam("dateFrom") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateFrom,
            @RequestParam("dateTo") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateTo,
            @RequestParam("destination") String destination) {
        try {
            return ResponseEntity.ok(hotelService.findAvailableRooms(destination, dateFrom, dateTo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}