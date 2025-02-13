package com.hackaboss.travelagency.dto.response;



import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HotelDTOResponse {

    private String hotelCode;
    private String name;
    private String city;
    private String roomType;
    private Double ratePerNight;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private String booked;
}
