package com.hackaboss.travelagency.mapper;
import com.hackaboss.travelagency.dto.response.HotelBookingDTOResponse;
import com.hackaboss.travelagency.dto.response.UserDTOResponse;
import com.hackaboss.travelagency.model.Hotel;
import com.hackaboss.travelagency.model.HotelBooking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface HotelBookingMapper {

    @Mapping(target = "dateFrom", source = "dateFrom")
    @Mapping(target = "dateTo", source = "dateTo")
    // Calcula el número de noches entre dateFrom y dateTo
    @Mapping(target = "nights", expression = "java( hotel.getDateFrom() != null && hotel.getDateTo() != null ? (int) ChronoUnit.DAYS.between(hotel.getDateFrom().toLocalDate(), hotel.getDateTo().toLocalDate()) : 0 )")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "hotelCode", source = "hotelCode")
    // Define peopleQuantity como el número de reservas asociadas al hotel
    @Mapping(target = "peopleQuantity", expression = "java( hotel.getListHotelBookings() != null ? hotel.getListHotelBookings().size() : 0 )")
    // Convierte roomType a String
    @Mapping(target = "roomType", expression = "java( hotel.getRoomType() != null ? hotel.getRoomType().toString() : null )")
    // Mapea la lista de reservas a listHosts usando el método auxiliar mapBookingsToHosts
    @Mapping(target = "listHosts", source = "listHotelBookings", qualifiedByName = "mapBookingsToHosts")
    HotelBookingDTOResponse toDTO(Hotel hotel);

    // Método auxiliar que convierte la lista de HotelBooking en una lista de UserDTOResponse
    // utilizando el UserMapper para cada reserva
    @Named("mapBookingsToHosts")
    default List<UserDTOResponse> mapBookingsToHosts(List<HotelBooking> bookings) {
        if (bookings == null) {
            return null;
        }
        return bookings.stream()
                .map(booking -> getUserMapper().toDTO(booking.getUser()))
                .distinct()  // Elimina duplicados en caso de que un mismo usuario aparezca en varias reservas
                .collect(Collectors.toList());
    }

    // Método para inyectar el UserMapper
    UserMapper getUserMapper();
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
