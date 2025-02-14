package com.hackaboss.travelagency.mapper;

import com.hackaboss.travelagency.dto.response.HotelBookingDTOResponse;
import com.hackaboss.travelagency.dto.response.UserDTOResponse;
import com.hackaboss.travelagency.model.HotelBooking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import com.hackaboss.travelagency.model.User;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface HotelBookingMapper {

    @Mapping(target = "dateFrom",
            expression = "java( booking.getHotel() != null ? booking.getHotel().getDateFrom() : null )")
    @Mapping(target = "dateTo",
            expression = "java( booking.getHotel() != null ? booking.getHotel().getDateTo() : null )")
    @Mapping(target = "nights",
            expression = "java( (booking.getHotel() != null && booking.getHotel().getDateFrom() != null && booking.getHotel().getDateTo() != null) "
                    + "? (int) java.time.temporal.ChronoUnit.DAYS.between(booking.getHotel().getDateFrom(), booking.getHotel().getDateTo()) "
                    + ": 0 )")
    // Mapeas city, hotelCode, etc. también desde booking.getHotel() si allí está la información
    @Mapping(target = "city",
            expression = "java( booking.getHotel() != null ? booking.getHotel().getCity() : null )")
    @Mapping(target = "hotelCode",
            expression = "java( booking.getHotel() != null ? booking.getHotel().getHotelCode() : null )")
    @Mapping(target = "peopleQuantity",
            expression = "java( booking.getHosts() != null ? booking.getHosts().size() : 0 )")
    // Ejemplo para mapear la lista de hosts a un DTO de usuarios
    @Mapping(target = "listHosts", source = "hosts", qualifiedByName = "mapHostsToUserDTOResponse")
    HotelBookingDTOResponse toDTO(HotelBooking booking);

    // Método auxiliar para convertir List<User> en List<UserDTOResponse>
    @Named("mapHostsToUserDTOResponse")
    default List<UserDTOResponse> mapHostsToUserDTOResponse(List<User> hosts) {
        if (hosts == null) {
            return new ArrayList<>();
        }
        UserMapper userMapper = Mappers.getMapper(UserMapper.class);
        return hosts.stream()
                .map(userMapper::toDTO)
                .distinct()
                .toList();
    }
}





//-------------------
/*
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface HotelBookingMapper {


     * Convierte una lista de HotelBooking (asumiendo que corresponden al mismo hotel)
     * en un HotelBookingDTOResponse que agrupa la información del hotel y
     * extrae la lista de hosts (usuarios) asociados.

    default HotelBookingDTOResponse toDTO(List<HotelBooking> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }
        // Se asume que todas las reservas pertenecen al mismo hotel.
        Hotel hotel = bookings.get(0).getHotel();

        int nights = 0;
        if (hotel.getDateFrom() != null && hotel.getDateTo() != null) {
            nights = (int) ChronoUnit.DAYS.between(
                    hotel.getDateFrom().toLocalDate(),
                    hotel.getDateTo().toLocalDate()
            );
        }

        // Se obtienen los hosts (usuarios) de cada reserva y se eliminan duplicados.
        List<UserDTOResponse> hosts = bookings.stream()
                .map(booking -> getUserMapper().toDTO(booking.getUser()))
                .distinct()
                .collect(Collectors.toList());

        // La cantidad de personas se define aquí como el número de reservas.
        int peopleQuantity = bookings.size();

        return HotelBookingDTOResponse.builder()
                .dateFrom(hotel.getDateFrom())
                .dateTo(hotel.getDateTo())
                .nights(nights)
                .city(hotel.getCity())
                .hotelCode(hotel.getHotelCode())
                .peopleQuantity(peopleQuantity)
                .roomType(hotel.getRoomType() != null ? hotel.getRoomType().toString() : null)
                .listHosts(hosts)
                .build();
    }

    // Método abstracto para inyectar el UserMapper, de modo que se pueda reutilizar su lógica
    UserMapper getUserMapper();
}*/
