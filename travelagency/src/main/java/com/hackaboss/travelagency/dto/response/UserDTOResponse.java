package com.hackaboss.travelagency.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTOResponse {

    private String name;
    private String surname;
    private String phone;
    private String dni;

}
