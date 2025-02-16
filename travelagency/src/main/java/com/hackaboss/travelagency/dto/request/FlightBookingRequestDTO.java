package com.hackaboss.travelagency.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightBookingRequestDTO {

    private Long id;
    private FlightDTORequest flight;
    private List<UserDTORequest> passengers;
}