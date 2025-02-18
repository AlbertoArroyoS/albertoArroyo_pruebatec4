package com.hackaboss.travelagency.dto.response;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightDTOResponse {

    private String flightNumber;
    private String origin;
    private String destination;
    private String seatType;
    private Double ratePerPerson;
    private LocalDate departureDate;
    private LocalDate returnDate;

}
