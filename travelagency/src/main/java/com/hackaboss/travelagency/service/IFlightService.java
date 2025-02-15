package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.FlightDTORequest;
import com.hackaboss.travelagency.dto.response.FlightDTOResponse;

import java.time.LocalDate;
import java.util.List;

public interface IFlightService {

    List<FlightDTOResponse> findAll();
    String createFlight (FlightDTORequest flightDTORequest);
    List<FlightDTOResponse> findAvailableFlights(String origin, String destination, LocalDate departureDate, LocalDate returnDate);
}
