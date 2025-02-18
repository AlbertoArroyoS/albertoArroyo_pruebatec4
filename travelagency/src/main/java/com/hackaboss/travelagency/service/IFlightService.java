package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.FlightDTORequest;
import com.hackaboss.travelagency.dto.response.FlightDTOResponse;
import com.hackaboss.travelagency.model.Flight;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IFlightService {

    List<FlightDTOResponse> findAll();
    String createFlight(FlightDTORequest flightDTORequest);
    String updateFlight(Long id, FlightDTORequest flightDTORequest);
    String deleteFlight(Long id);
    Optional<FlightDTOResponse> findById(Long id);
    List<FlightDTOResponse> findAvailableFlights(String origin, String destination, LocalDate departureDate, LocalDate returnDate);
    Flight findActiveFlight(FlightDTORequest flightDTORequest);
    Flight save(Flight flight);
}
