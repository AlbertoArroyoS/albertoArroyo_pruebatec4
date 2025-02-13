package com.hackaboss.travelagency.dto.response;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class FlightDTOResponse {

    private String flightNumber;
    private String origin;
    private String destination;
    private String seatType;
    private Double ratePerPerson;
    private LocalDate departureDate;
    private LocalDate returnDate;

}
