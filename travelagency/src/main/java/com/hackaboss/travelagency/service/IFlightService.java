package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.FlightDTORequest;
import com.hackaboss.travelagency.dto.response.FlightDTOResponse;

import java.util.List;

public interface IFlightService {

    List<FlightDTOResponse> findAll();
    String createFlight (FlightDTORequest flightDTORequest);
}
