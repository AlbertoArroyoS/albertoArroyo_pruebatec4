package com.hackaboss.travelagency.mapper;

import com.hackaboss.travelagency.dto.response.HotelDTOResponse;
import com.hackaboss.travelagency.dto.response.UserDTOResponse;
import com.hackaboss.travelagency.model.Hotel;
import com.hackaboss.travelagency.model.HotelBooking;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class},
        builder = @Builder(disableBuilder = true))
public interface HotelMapper {

    @Mapping(target = "dateFrom", source = "dateFrom")
    @Mapping(target = "dateTo", source = "dateTo")
    // Calcula la cantidad de noches entre dateFrom y dateTo
    @Mapping(target = "nights", expression = "java( hotel.getDateFrom() != null && hotel.getDateTo() != null ? (int) ChronoUnit.DAYS.between(hotel.getDateFrom().toLocalDate(), hotel.getDateTo().toLocalDate()) : 0 )")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "hotelCode", source = "hotelCode")
    // Define peopleQuantity como el número de reservas (ajusta según tu lógica)
    @Mapping(target = "peopleQuantity", expression = "java( hotel.getListHotelBookings() != null ? hotel.getListHotelBookings().size() : 0 )")
    // Convierte roomType a String
    @Mapping(target = "roomType", expression = "java( hotel.getRoomType() != null ? hotel.getRoomType().toString() : null )")
    // Mapea la lista de reservas a listHosts usando el método auxiliar mapBookingsToHosts
    @Mapping(target = "listHosts", source = "listHotelBookings", qualifiedByName = "mapBookingsToHosts")
    HotelDTOResponse toDTO(Hotel hotel);

    // Método auxiliar que mapea la lista de HotelBooking a una lista de UserDTOResponse
    // utilizando el UserMapper inyectado (se asume que cada HotelBooking tiene un User asociado)
    @Named("mapBookingsToHosts")
    default List<UserDTOResponse> mapBookingsToHosts(List<HotelBooking> bookings) {
        if (bookings == null) {
            return null;
        }
        return bookings.stream()
                .map(booking -> getUserMapper().toDTO(booking.getUser()))
                .distinct() // si deseas eliminar duplicados
                .collect(Collectors.toList());
    }

    // Este método inyecta el UserMapper para poder reutilizar su lógica de conversión
    UserMapper getUserMapper();
}
