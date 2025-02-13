package com.hackaboss.travelagency.mapper;

import com.hackaboss.travelagency.dto.response.FlightDTOResponse;
import com.hackaboss.travelagency.model.Flight;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FlightMapper {

    FlightDTOResponse entityToDTO(Flight flight);
}
