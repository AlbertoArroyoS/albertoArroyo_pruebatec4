package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.FlightBookingRequestDTO;
import com.hackaboss.travelagency.dto.request.UserDTORequest;
import com.hackaboss.travelagency.dto.response.FlightBookingResponseDTO;
import com.hackaboss.travelagency.dto.response.UserDTOResponse;
import com.hackaboss.travelagency.exception.EntityNotDeletableException;
import com.hackaboss.travelagency.exception.InvalidDataException;
import com.hackaboss.travelagency.model.Flight;
import com.hackaboss.travelagency.model.FlightBooking;
import com.hackaboss.travelagency.model.User;
import com.hackaboss.travelagency.repository.FlightBookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightBookingService implements IFlightBookingService {

    private final IFlightService flightService;
    private final IUserService userService;
    private final FlightBookingRepository flightBookingRepository;

    public FlightBookingService(IFlightService flightService, IUserService userService, FlightBookingRepository flightBookingRepository) {
        this.flightService = flightService;
        this.userService = userService;
        this.flightBookingRepository = flightBookingRepository;
    }

    @Override
    @Transactional
    public String bookFlight(FlightBookingRequestDTO request) {
        Flight flight = flightService.findActiveFlight(request.getFlight());

        // Procesar los pasajeros
        List<User> passengers = new ArrayList<>();
        for (UserDTORequest hostDTO : request.getPassengers()) {
            if (hostDTO.getDni() == null || hostDTO.getDni().isBlank()) {
                throw new InvalidDataException("DNI del pasajero no puede ser nulo o vacío");
            }
            User host = userService.findOrCreateUserByDni(hostDTO);
            passengers.add(host);
        }

        // Verificar si ya existe una reserva activa para este vuelo y estos pasajeros
        boolean existsBooking = flightBookingRepository.existsByFlightAndPassengersInAndActiveTrue(flight, passengers);
        if (existsBooking) {
            throw new EntityNotDeletableException("Ya existe una reserva activa para este vuelo con los mismos pasajeros.");
        }

        // Crear la reserva
        FlightBooking booking = requestToEntity(request);
        booking.setFlight(flight);
        booking.setPassengers(passengers);
        booking = flightBookingRepository.save(booking);

        flightService.save(flight);

        double totalAmount = flight.getRatePerPerson() * passengers.size();
        return "Reserva de vuelo creada correctamente con ID: " + booking.getId() + ", Monto total: " + totalAmount;
    }

    @Override
    public List<FlightBookingResponseDTO> findAll() {
        List<FlightBooking> activeBookings = flightBookingRepository.findByActiveTrue();
        return activeBookings.stream()
                .map(this::entityToDTO)
                .toList();
    }

    // Métodos de conversión
    private FlightBooking requestToEntity(FlightBookingRequestDTO request) {
        return FlightBooking.builder()
                .flight(flightService.findActiveFlight(request.getFlight()))
                .passengers(request.getPassengers().stream()
                        .filter(dto -> dto.getDni() != null && !dto.getDni().isBlank())
                        .map(userService::findOrCreateUserByDni)
                        .toList())
                .build();
    }

    private FlightBookingResponseDTO entityToDTO(FlightBooking booking) {
        return FlightBookingResponseDTO.builder()
                .flightNumber(booking.getFlight().getFlightNumber())
                .origin(booking.getFlight().getOrigin())
                .destination(booking.getFlight().getDestination())
                .departureDate(booking.getFlight().getDepartureDate())
                .numberOfPassengers(booking.getPassengers().size())
                .totalAmount(booking.getFlight().getRatePerPerson() * booking.getPassengers().size())
                .passengers(booking.getPassengers().stream().map(this::userToDTO).toList())
                .build();
    }

    private UserDTOResponse userToDTO(User user) {
        return UserDTOResponse.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .phone(user.getPhone())
                .dni(user.getDni())
                .build();
    }
}
