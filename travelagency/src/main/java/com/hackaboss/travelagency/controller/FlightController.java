package com.hackaboss.travelagency.controller;

import com.hackaboss.travelagency.dto.request.FlightDTORequest;
import com.hackaboss.travelagency.dto.response.FlightDTOResponse;
import com.hackaboss.travelagency.service.IFlightService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agency/flights")
public class FlightController {

    private final IFlightService flightService;

    public FlightController(IFlightService flightService) {
        this.flightService = flightService;
    }

    // GET /agency/flights - Obtiene el listado de todos los vuelos registrados
    @GetMapping
    public ResponseEntity<List<FlightDTOResponse>> getAllFlights() {
        List<FlightDTOResponse> flights = flightService.findAll();
        return ResponseEntity.ok(flights);
    }

    @PostMapping
    public ResponseEntity<String> createFlight(@Valid @RequestBody FlightDTORequest flightDTORequest) {
        String createdFlight = flightService.createFlight(flightDTORequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdFlight);
    }

}
