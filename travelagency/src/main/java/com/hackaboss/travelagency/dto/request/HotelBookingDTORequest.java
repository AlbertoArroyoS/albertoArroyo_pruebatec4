package com.hackaboss.travelagency.dto.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HotelBookingDTORequest {

    private Long id;
    private HotelDTORequest hotel;
    private List<UserDTORequest> hosts;

}
