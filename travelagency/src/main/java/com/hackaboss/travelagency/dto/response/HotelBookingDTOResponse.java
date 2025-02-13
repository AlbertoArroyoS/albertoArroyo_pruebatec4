package com.hackaboss.travelagency.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Builder
public class HotelBookingDTOResponse {

    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private Integer nights;
    private String city;
    private String hotelCode;
    private Integer peopleQuantity;
    private String roomType;
    private List<UserDTOResponse> listHosts;

}
