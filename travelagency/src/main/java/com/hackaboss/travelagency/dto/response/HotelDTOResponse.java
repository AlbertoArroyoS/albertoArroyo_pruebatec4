package com.hackaboss.travelagency.dto.response;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class HotelDTOResponse {

    private String hotelCode;
    private String name;
    private String city;
    private String roomType;
    private Double ratePerNight;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private String booked;
}
