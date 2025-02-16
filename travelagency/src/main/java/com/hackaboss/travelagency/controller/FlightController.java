package com.hackaboss.travelagency.controller;

import com.hackaboss.travelagency.dto.request.FlightDTORequest;
import com.hackaboss.travelagency.dto.response.FlightDTOResponse;
import com.hackaboss.travelagency.service.IFlightService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/agency/flights")
public class FlightController {

    private final IFlightService flightService;

    public FlightController(IFlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public ResponseEntity<List<FlightDTOResponse>> getAllFlights() {
        try {
            return ResponseEntity.ok(flightService.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<String> createFlight(@Valid @RequestBody FlightDTORequest flightDTORequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(flightService.createFlight(flightDTORequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updateFlight(@PathVariable Long id, @Valid @RequestBody FlightDTORequest flightDTORequest) {
        try {
            return ResponseEntity.ok(flightService.updateFlight(id, flightDTORequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(flightService.deleteFlight(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findFlightById(@PathVariable Long id) {
        Optional<FlightDTOResponse> flightOpt = flightService.findById(id);
        if (flightOpt.isPresent()) {
            return ResponseEntity.ok(flightOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró vuelo con ID: " + id);
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<FlightDTOResponse>> findAvailableFlights(
            @RequestParam("dateFrom") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateFrom,
            @RequestParam("dateTo") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateTo,
            @RequestParam("origin") String origin,
            @RequestParam("destination") String destination) {
        try {
            return ResponseEntity.ok(flightService.findAvailableFlights(origin, destination, dateFrom, dateTo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}