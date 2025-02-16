package com.hackaboss.travelagency.mapper;

import com.hackaboss.travelagency.dto.request.FlightBookingRequestDTO;
import com.hackaboss.travelagency.dto.response.FlightBookingResponseDTO;
import com.hackaboss.travelagency.model.FlightBooking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// Mapper para convertir entre entidades y DTOs con MapStruct
@Mapper(componentModel = "spring")
public interface FlightBookingMapper {

    @Mapping(source = "flight.flightNumber", target = "flightNumber")
    @Mapping(source = "flight.origin", target = "origin")
    @Mapping(source = "flight.destination", target = "destination")
    @Mapping(source = "flight.departureDate", target = "departureDate")
    @Mapping(source = "passengers", target = "passengers")
    @Mapping(target = "numberOfPassengers", expression = "java(booking.getPassengers() != null ? booking.getPassengers().size() : 0)")
    @Mapping(target = "totalAmount", expression = "java(booking.getFlight().getRatePerPerson() * booking.getPassengers().size())") // Se calculará en el servicio
    FlightBookingResponseDTO entityToDTO(FlightBooking booking);

    @Mapping(source = "flight", target = "flight")
    @Mapping(target = "passengers", ignore = true)
    FlightBooking requestToEntity(FlightBookingRequestDTO request);
}