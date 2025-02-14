package com.hackaboss.travelagency.mapper;

import com.hackaboss.travelagency.dto.request.UserDTORequest;
import com.hackaboss.travelagency.dto.response.UserDTOResponse;
import com.hackaboss.travelagency.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Convierte desde UserDTORequest a User (generalmente al crear o actualizar)
    User requestToEntity(UserDTORequest dto);

    // Convierte desde User a, por ejemplo, un UserDTOResponse (si lo necesitas)
    UserDTOResponse toDTO(User user);
}
