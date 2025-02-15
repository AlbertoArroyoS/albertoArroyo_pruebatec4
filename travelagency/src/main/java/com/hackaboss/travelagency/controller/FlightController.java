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

@RestController
@RequestMapping("/agency")
public class FlightController {

    private final IFlightService flightService;

    public FlightController(IFlightService flightService) {
        this.flightService = flightService;
    }

    // GET /agency/flights - Obtiene el listado de todos los vuelos registrados
    @GetMapping("/flights")
    public ResponseEntity<List<FlightDTOResponse>> getAllFlights() {
        List<FlightDTOResponse> flights = flightService.findAll();
        return ResponseEntity.ok(flights);
    }

    @PostMapping("/flights")
    public ResponseEntity<String> createFlight(@Valid @RequestBody FlightDTORequest flightDTORequest) {
        String createdFlight = flightService.createFlight(flightDTORequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdFlight);
    }

    @GetMapping("/flights/available")
    public List<FlightDTOResponse> findAvailableFlights(
            @RequestParam("dateFrom") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateFrom,
            @RequestParam("dateTo") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateTo,
            @RequestParam("origin") String origin,
            @RequestParam("destination") String destination) {
        return flightService.findAvailableFlights(origin, destination, dateFrom, dateTo);
    }

}
