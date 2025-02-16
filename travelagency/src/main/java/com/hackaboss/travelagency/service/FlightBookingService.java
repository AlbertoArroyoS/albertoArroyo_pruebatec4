package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.FlightBookingRequestDTO;
import com.hackaboss.travelagency.dto.request.UserDTORequest;
import com.hackaboss.travelagency.mapper.FlightBookingMapper;
import com.hackaboss.travelagency.model.Flight;
import com.hackaboss.travelagency.model.FlightBooking;
import com.hackaboss.travelagency.model.User;
import com.hackaboss.travelagency.repository.FlightBookingRepository;
import com.hackaboss.travelagency.repository.FlightRepository;
import com.hackaboss.travelagency.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                    .orElseThrow(() -> new EntityNotFoundException("Vueldo no encontrado"));
        }

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

        passengers = userRepository.saveAll(passengers);

        FlightBooking booking = flightBookingMapper.requestToEntity(request);
        booking.setFlight(flight);
        booking.setPassengers(passengers);
        booking = flightBookingRepository.save(booking);

        Double totalAmount = flight.getRatePerPerson() * passengers.size();
        return "Reserva de vuelo creada correctamente con ID: " + booking.getId() + ", Monto total: " + totalAmount;
    }
}
