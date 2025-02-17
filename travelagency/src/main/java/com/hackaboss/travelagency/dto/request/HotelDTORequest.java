package com.hackaboss.travelagency.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Data
@Builder
public class HotelDTORequest {

    private Long id;

    @NotNull(message = "El codigo del hotel no puede ser nulo")
    @NotBlank(message = "El codigo del hotel no puede estar vacío")
    @Size(min = 2, max = 250, message = "El codigo del hotel debe tener entre 2 y 250 caracteres")
    private String hotelCode;

    @NotNull(message = "El nombre del hotel no puede ser nulo")
    @NotBlank(message = "El nombre del hotel no puede estar vacío")
    @Size(min = 2, max = 250, message = "El nombre del hotel debe tener entre 2 y 250 caracteres")
    private String name;

    @NotNull(message = "La ciudad del hotel no puede ser nula")
    @NotBlank(message = "La ciudad del hotel no puede estar vacía")
    @Size(min = 2, max = 250, message = "La ciudad del hotel debe tener entre 2 y 250 caracteres")
    private String city;

    @NotNull(message = "El tipo de habitación no puede ser nulo")
    @NotBlank(message = "El tipo de habitación no puede estar vacío")
    @Size(min = 2, max = 250, message = "El tipo de habitación debe tener entre 2 y 250 caracteres")
    private String roomType;

    @NotNull(message = "La tarifa por noche no puede ser nula")
    @DecimalMin(value = "0.01", message = "El precio debe ser al menos 0.01")
    private Double ratePerNight;

    @NotNull(message = "La fecha de inicio no puede ser nula")
    private LocalDate dateFrom;

    @NotNull(message = "La fecha de fin no puede ser nula")
    private LocalDate dateTo;


}