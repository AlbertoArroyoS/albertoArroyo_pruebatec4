package com.hackaboss.travelagency.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class FlightDTORequest {

    @NotNull(message = "El número de vuejo no puede ser nulo")
    @NotBlank(message = "El número de vuelo no puede estar vacío")
    @Size(min = 2, max = 250, message = "El número del vuelo debe tener entre 2 y 250 caracteres")
    private String flightNumber;

    @NotNull(message = "El origen no puede ser nulo")
    @NotBlank(message = "El origen no puede estar vacío")
    @Size(min = 2, max = 250, message = "El origen debe tener entre 2 y 250 caracteres")
    private String origin;

    @NotNull(message = "El destino no puede ser nulo")
    @NotBlank(message = "El destino no puede estar vacío")
    @Size(min = 2, max = 250, message = "El destino debe tener entre 2 y 250 caracteres")
    private String destination;

    @NotNull(message = "El tipo de asiento no puede ser nulo")
    @NotBlank(message = "El tipo de asiento no puede estar vacío")
    @Size(min = 2, max = 250, message = "El tipo de asiento debe tener entre 2 y 250 caracteres")
    private String seatType;
    private Double ratePerPerson;
    private LocalDate departureDate;
    private LocalDate returnDate;
}
