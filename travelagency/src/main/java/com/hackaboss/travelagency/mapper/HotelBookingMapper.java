package com.hackaboss.travelagency.mapper;

import com.hackaboss.travelagency.dto.request.HotelBookingDTORequest;
import com.hackaboss.travelagency.dto.request.UserDTORequest;
import com.hackaboss.travelagency.dto.response.HotelBookingDTOResponse;
import com.hackaboss.travelagency.dto.response.UserDTOResponse;
import com.hackaboss.travelagency.model.HotelBooking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.hackaboss.travelagency.model.User;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface HotelBookingMapper {

    // ----------------------------------------------------
    // 1) ENTIDAD → DTO (HotelBooking -> HotelBookingDTOResponse)
    // ----------------------------------------------------
    @Mapping(target = "dateFrom",
            expression = "java( booking.getHotel() != null ? booking.getHotel().getDateFrom() : null )")
    @Mapping(target = "dateTo",
            expression = "java( booking.getHotel() != null ? booking.getHotel().getDateTo() : null )")
    @Mapping(target = "nights",
            expression = "java( (booking.getHotel() != null && booking.getHotel().getDateFrom() != null && booking.getHotel().getDateTo() != null) "
                    + "? (int) java.time.temporal.ChronoUnit.DAYS.between(booking.getHotel().getDateFrom(), booking.getHotel().getDateTo()) "
                    + ": 0 )")
    @Mapping(target = "city",
            expression = "java( booking.getHotel() != null ? booking.getHotel().getCity() : null )")
    @Mapping(target = "hotelCode",
            expression = "java( booking.getHotel() != null ? booking.getHotel().getHotelCode() : null )")
    @Mapping(target = "peopleQuantity",
            expression = "java( booking.getHosts() != null ? booking.getHosts().size() : 0 )")
    @Mapping(target = "roomType",
            expression = "java( (booking.getHotel() != null && booking.getHotel().getRoomType() != null) "
                    + "? booking.getHotel().getRoomType().toString() : null )")
    // Mapea la lista de hosts (User) a listHosts en el DTO (UserDTOResponse)
    @Mapping(target = "hosts", source = "hosts", qualifiedByName = "mapHostsToUserDTOResponse")
    HotelBookingDTOResponse entityToDTO(HotelBooking booking);

    // ----------------------------------------------------
    // 2) DTO (Request) → ENTIDAD (HotelBooking)
    // ----------------------------------------------------
    @Mapping(target = "hotel", ignore = true)
    // Se ignora porque normalmente buscarás el hotel por hotelCode en el Service

    @Mapping(target = "user", ignore = true)
    // Si necesitas un usuario principal, podrías mapearlo con un método auxiliar
    // o buscarlo en el Service. Por ahora, lo ignoramos.

    @Mapping(target = "hosts", source = "listPassengers", qualifiedByName = "mapPassengersDTOToUsers")
    HotelBooking requestToEntity(HotelBookingDTORequest dto);

    // ----------------------------------------------------
    // MÉTODOS AUXILIARES
    // ----------------------------------------------------

    // (A) ENTIDAD → DTO (Users)
    @Named("mapHostsToUserDTOResponse")
    default List<UserDTOResponse> mapHostsToUserDTOResponse(List<User> hosts) {
        if (hosts == null) {
            return new ArrayList<>();
        }
        UserMapper userMapper = Mappers.getMapper(UserMapper.class);
        return hosts.stream()
                .map(userMapper::toDTO)
                .distinct()
                .collect(Collectors.toList());
    }

    // (B) DTO (Request) → ENTIDAD (Users)
    @Named("mapPassengersDTOToUsers")
    default List<User> mapPassengersDTOToUsers(List<UserDTORequest> passengerDTOs) {
        if (passengerDTOs == null) {
            return new ArrayList<>();
        }
        UserMapper userMapper = Mappers.getMapper(UserMapper.class);
        return passengerDTOs.stream()
                .map(userMapper::requestToEntity)  // Crea o asocia el User
                .collect(Collectors.toList());
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
