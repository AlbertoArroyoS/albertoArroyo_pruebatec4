package com.hackaboss.travelagency.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class UserDTORequest {

    private Long id;

    @NotNull(message = "El nombre del usuario no puede ser nulo")
    @NotBlank(message = "El nombre del usuario no puede estar vacío")
    private String name;

    @NotNull(message = "El apellido del usuario no puede ser nulo")
    @NotBlank(message = "El apellido del usuario no puede estar vacío")
    private String surname;

    private String phone;

    @NotNull(message = "El DNI del usuario no puede ser nulo")
    @NotBlank(message = "El DNI del usuario no puede estar vacío")
    private String dni;

}
