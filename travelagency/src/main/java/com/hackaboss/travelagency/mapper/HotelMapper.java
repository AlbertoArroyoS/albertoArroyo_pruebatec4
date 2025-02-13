package com.hackaboss.travelagency.mapper;

import com.hackaboss.travelagency.dto.response.HotelDTOResponse;
import com.hackaboss.travelagency.dto.response.UserDTOResponse;
import com.hackaboss.travelagency.model.Hotel;
import com.hackaboss.travelagency.model.HotelBooking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Mapper(componentModel = "spring")
public interface HotelMapper {

    @Mapping(target = "nights", expression = "java(calculateNights(hotel.getDateFrom(), hotel.getDateTo()))")
    @Mapping(target = "roomType", source = "roomType")
    @Mapping(target = "peopleQuantity", expression = "java(hotel.getListHotelBookings() != null ? hotel.getListHotelBookings().size() : 0)")
    @Mapping(target = "listHosts", source = "listHotelBookings")
    HotelDTOResponse hotelToHotelDTOResponse(Hotel hotel);

    // Si existe un mapeo de HotelBooking a UserDTOResponse, lo puedes definir o utilizar otro mapper
    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    UserDTOResponse hotelBookingToUserDTOResponse(HotelBooking hotelBooking);

    default int calculateNights(LocalDateTime dateFrom, LocalDateTime dateTo) {
        if(dateFrom == null || dateTo == null) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(dateFrom.toLocalDate(), dateTo.toLocalDate());
    }
}
