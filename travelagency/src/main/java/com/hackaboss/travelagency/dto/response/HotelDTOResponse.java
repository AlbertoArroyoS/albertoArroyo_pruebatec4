package com.hackaboss.travelagency.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class HotelDTOResponse {

    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private Integer nights;
    private String city;
    private String hotelCode;
    private Integer peopleQuantity;
    private String roomType;
    private List<UserDTOResponse> listHosts;

}
