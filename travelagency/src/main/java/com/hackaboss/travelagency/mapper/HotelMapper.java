package com.hackaboss.travelagency.mapper;

import com.hackaboss.travelagency.dto.request.HotelDTORequest;
import com.hackaboss.travelagency.dto.response.HotelDTOResponse;
import com.hackaboss.travelagency.model.Hotel;
import com.hackaboss.travelagency.util.RoomTypeConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {RoomTypeConverter.class})
public interface HotelMapper {

    @Mapping(target = "roomType", expression = "java(hotel.getRoomType() != null ? hotel.getRoomType().toString() : null)")
    @Mapping(target = "booked", expression = "java(hotel.getBooked() != null ? hotel.getBooked().toString() : null)")
    HotelDTOResponse entityToDTO(Hotel hotel);

    @Mapping(target = "roomType", expression = "java(RoomTypeConverter.fromString(hotelDTORequest.getRoomType()))")
    Hotel requestToEntity(HotelDTORequest hotelDTORequest);
}
