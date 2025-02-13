package com.hackaboss.travelagency.mapper;

import com.hackaboss.travelagency.dto.response.UserDTOResponse;
import com.hackaboss.travelagency.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Mapea la entidad User a UserDTOResponse
    UserDTOResponse toDTO(User user);
}
