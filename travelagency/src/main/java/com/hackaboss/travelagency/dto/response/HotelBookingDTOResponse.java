package com.hackaboss.travelagency.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
@Builder
public class HotelBookingDTOResponse {

    private LocalDate dateFrom;
    private LocalDate dateTo;
    private Integer nights;
    private String city;
    private String hotelCode;
    private Integer peopleQuantity;
    private String roomType;
    private List<UserDTOResponse> listHosts;

}
