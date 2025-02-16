package com.hackaboss.travelagency.controller;

import com.hackaboss.travelagency.dto.request.FlightBookingRequestDTO;
import com.hackaboss.travelagency.service.IFlightBookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/agency/flight-booking")
public class FlightBookingController {

    private final IFlightBookingService flightBookingService;

    public FlightBookingController(IFlightBookingService flightBookingService) {
        this.flightBookingService = flightBookingService;
    }


    @PostMapping("/new")
    public ResponseEntity<String> createFlightBooking(@RequestBody FlightBookingRequestDTO request) {
        String response = flightBookingService.bookFlight(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
