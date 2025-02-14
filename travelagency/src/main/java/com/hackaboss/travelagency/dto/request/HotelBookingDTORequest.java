package com.hackaboss.travelagency.dto.request;

import com.hackaboss.travelagency.dto.response.UserDTOResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class HotelBookingDTORequest {

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate dateFrom;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate dateTo;

    @NotNull(message = "La ciudad es obligatoria")
    @NotBlank(message = "La ciudad no puede estar vacía")
    private String city;

    @NotNull(message = "El código del hotel es obligatorio")
    @NotBlank(message = "El código del hotel no puede estar vacío")
    private String hotelCode;

    @NotNull(message = "La cantidad de personas es obligatoria")
    private Integer peopleQuantity;

    @NotNull(message = "El tipo de habitación es obligatorio")
    @NotBlank(message = "El tipo de habitación no puede estar vacío")
    private String roomType;

    private List<UserDTORequest> listPassengers;

}
