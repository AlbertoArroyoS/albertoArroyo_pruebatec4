package com.hackaboss.travelagency.dto.request;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class UserDTORequest {

    private String name;
    private String surname;
    private String phone;
    private String dni;

}
