package com.hackaboss.travelagency.dto.request;

import com.hackaboss.travelagency.dto.response.UserDTOResponse;
import com.hackaboss.travelagency.model.Hotel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class HotelBookingDTORequest {

    private Long id;
    private HotelDTORequest hotel;
    private UserDTORequest user;
    private List<UserDTORequest> listPassengers;

}
