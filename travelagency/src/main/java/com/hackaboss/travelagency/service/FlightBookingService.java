package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.FlightBookingRequestDTO;
import com.hackaboss.travelagency.dto.request.UserDTORequest;
import com.hackaboss.travelagency.dto.response.FlightBookingResponseDTO;
import com.hackaboss.travelagency.dto.response.FlightDTOResponse;
import com.hackaboss.travelagency.exception.InvalidDataException;
import com.hackaboss.travelagency.mapper.FlightBookingMapper;
import com.hackaboss.travelagency.model.Flight;
import com.hackaboss.travelagency.model.FlightBooking;
import com.hackaboss.travelagency.model.User;
import com.hackaboss.travelagency.repository.FlightBookingRepository;
import com.hackaboss.travelagency.repository.FlightRepository;
import com.hackaboss.travelagency.repository.UserRepository;
import com.hackaboss.travelagency.util.Booked;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightBookingService implements IFlightBookingService {

    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final FlightBookingRepository flightBookingRepository;
    private final FlightBookingMapper flightBookingMapper;

    public FlightBookingService(FlightRepository flightRepository, UserRepository userRepository, FlightBookingRepository flightBookingRepository, FlightBookingMapper flightBookingMapper) {
        this.flightRepository = flightRepository;
        this.userRepository = userRepository;
        this.flightBookingRepository = flightBookingRepository;
        this.flightBookingMapper = flightBookingMapper;
    }
    @Override
    @Transactional
    public String bookFlight(FlightBookingRequestDTO request) {
        Flight flight;
        if (request.getFlight().getId() != null) {
            flight = flightRepository.findByIdAndActiveTrue(request.getFlight().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Vuelo no encontrado"));
        } else {
            flight = flightRepository.findByFlightNumberAndActiveTrue(request.getFlight().getFlightNumber())
                    .orElseThrow(() -> new EntityNotFoundException("Vuelo no encontrado"));
        }

        // Procesar los pasajeros
        List<User> passengers = new ArrayList<>();
        for (UserDTORequest passengerDTO : request.getPassengers()) {
            User passenger = userRepository.findByDni(passengerDTO.getDni())
                    .orElseGet(() -> {
                        User newUser = User.builder()
                                .dni(passengerDTO.getDni())
                                .username(passengerDTO.getDni())
                                .name(passengerDTO.getName())
                                .surname(passengerDTO.getSurname())
                                .phone(passengerDTO.getPhone())
                                .build();
                        return userRepository.save(newUser);
                    });
            passengers.add(passenger);
        }

        // Verificar si ya existe una reserva activa para este vuelo y estos pasajeros
        boolean existsBooking = flightBookingRepository.existsByFlightAndPassengersInAndActiveTrue(flight, passengers);
        if (existsBooking) {
            return "Ya existe una reserva activa para este vuelo con los mismos pasajeros.";
        }

        // Crear la reserva
        FlightBooking booking = flightBookingMapper.requestToEntity(request);
        booking.setFlight(flight);
        booking.setPassengers(passengers);
        booking = flightBookingRepository.save(booking);

        flightRepository.save(flight);

        Double totalAmount = flight.getRatePerPerson() * passengers.size();
        return "Reserva de vuelo creada correctamente con ID: " + booking.getId() + ", Monto total: " + totalAmount;
    }


    @Override
    public List<FlightBookingResponseDTO> findAll() {
        List<FlightBooking> activeBookings = flightBookingRepository.findByActiveTrue();
        return activeBookings.stream()
                .map(flightBookingMapper::entityToDTO)
                .toList();
    }


}
