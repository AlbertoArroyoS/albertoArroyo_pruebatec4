package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.FlightBookingRequestDTO;
import com.hackaboss.travelagency.dto.response.FlightBookingResponseDTO;

public interface IFlightBookingService {

    String bookFlight(FlightBookingRequestDTO request);
}
