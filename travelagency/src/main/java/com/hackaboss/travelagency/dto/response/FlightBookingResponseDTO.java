package com.hackaboss.travelagency.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightBookingResponseDTO {

    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDate departureDate;
    private Integer numberOfPassengers;
    private Double totalAmount;
    private List<UserDTOResponse> passengers;
}
