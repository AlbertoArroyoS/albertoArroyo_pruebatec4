package com.hackaboss.travelagency.mapper;

import com.hackaboss.travelagency.dto.request.HotelDTORequest;
import com.hackaboss.travelagency.dto.response.HotelDTOResponse;
import com.hackaboss.travelagency.model.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    @Mapping(target = "roomType", expression = "java(hotel.getRoomType() != null ? hotel.getRoomType().toString() : null)")
    @Mapping(target = "booked", expression = "java(hotel.getBooked() != null ? hotel.getBooked().toString() : null)")
    HotelDTOResponse entityToDTO(Hotel hotel);

    Hotel requestToEntity(HotelDTORequest hotelDTORequest);
}
