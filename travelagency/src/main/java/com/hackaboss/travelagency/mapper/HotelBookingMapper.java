package com.hackaboss.travelagency.mapper;

import com.hackaboss.travelagency.dto.request.HotelBookingDTORequest;
import com.hackaboss.travelagency.dto.response.HotelBookingDTOResponse;
import com.hackaboss.travelagency.model.HotelBooking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.time.temporal.ChronoUnit; // Opcional si se usa el nombre completo en la expresión

@Mapper(componentModel = "spring", uses = {HotelMapper.class, UserMapper.class})
public interface HotelBookingMapper {

    // Mapea de DTORequest a Entidad
    HotelBooking requestToEntity(HotelBookingDTORequest dto);

    // Mapea de Entidad a DTOResponse, realizando algunos cálculos en campos derivados
    @Mapping(target = "dateFrom", source = "hotel.dateFrom")
    @Mapping(target = "dateTo", source = "hotel.dateTo")
    @Mapping(target = "city", source = "hotel.city")
    @Mapping(target = "hotelCode", source = "hotel.hotelCode")
    @Mapping(target = "roomType", source = "hotel.roomType")
    @Mapping(
            target = "nights",
            expression = "java((int) java.time.temporal.ChronoUnit.DAYS.between(booking.getHotel().getDateFrom(), booking.getHotel().getDateTo()))"
    )
    @Mapping(
            target = "price",
            expression = "java(((int) java.time.temporal.ChronoUnit.DAYS.between(booking.getHotel().getDateFrom(), booking.getHotel().getDateTo())) * booking.getHotel().getRatePerNight())"
    )
    @Mapping(
            target = "peopleQuantity",
            expression = "java(booking.getHosts() != null ? booking.getHosts().size() : 0)"
    )
    HotelBookingDTOResponse entityToDTO(HotelBooking booking);
}
