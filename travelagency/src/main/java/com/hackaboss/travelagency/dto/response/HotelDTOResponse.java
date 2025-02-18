package com.hackaboss.travelagency.dto.response;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
