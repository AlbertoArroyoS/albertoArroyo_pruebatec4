package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.FlightDTORequest;
import com.hackaboss.travelagency.dto.response.FlightDTOResponse;
import com.hackaboss.travelagency.mapper.FlightMapper;
import com.hackaboss.travelagency.model.Flight;
import com.hackaboss.travelagency.repository.FlightBookingRepository;
import com.hackaboss.travelagency.repository.FlightRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService implements IFlightService {

    private final FlightMapper flightMapper;
    private final FlightRepository flightRepository;
    private final FlightBookingRepository flightBookingRepository;

    public FlightService(FlightMapper flightMapper, FlightRepository flightRepository, FlightBookingRepository flightBookingRepository) {
        this.flightMapper = flightMapper;
        this.flightRepository = flightRepository;
        this.flightBookingRepository = flightBookingRepository;
    }


    @Override
    public List<FlightDTOResponse> findAll() {
        return flightRepository.findByActiveTrue().stream()
                .map(flightMapper::entityToDTO)
                .toList();
    }

    @Override
    @Transactional
    public String createFlight(FlightDTORequest flightDTORequest) {
        if (flightDTORequest == null) {
            throw new RuntimeException("Datos del vuelo no pueden ser nulos. No se pudo crear.");
        }
        Flight flight = flightMapper.requestToEntity(flightDTORequest);
        flightRepository.save(flight);
        return "Vuelo creado con éxito";
    }

    @Override
    @Transactional
    public String updateFlight(Long id, FlightDTORequest flightDTORequest) {
        if (id == null || flightDTORequest == null) {
            throw new RuntimeException("Datos inválidos para actualizar el vuelo. No se pudo actualizar.");
        }
        Flight flight = flightRepository.findByIdAndActiveTrue(id)
                .orElse(null);

        if (flight == null) {
            throw new RuntimeException("No se encontró el vuelo con ID " + id + ". No se pudo actualizar.");
        }

        flight.setFlightNumber(flightDTORequest.getFlightNumber());
        flight.setOrigin(flightDTORequest.getOrigin());
        flight.setDestination(flightDTORequest.getDestination());
        flight.setSeatType(flightDTORequest.getSeatType());
        flight.setRatePerPerson(flightDTORequest.getRatePerPerson());
        flight.setDepartureDate(flightDTORequest.getDepartureDate());
        flight.setReturnDate(flightDTORequest.getReturnDate());

        flightRepository.save(flight);
        return "Vuelo actualizado con éxito";
    }

    @Override
    @Transactional
    public String deleteFlight(Long id) {
        if (id == null) {
            throw new RuntimeException("ID del vuelo no puede ser nulo. No se pudo eliminar.");
        }
        Flight flight = flightRepository.findByIdAndActiveTrue(id)
                .orElse(null);

        if (flight == null) {
            throw new RuntimeException("No se encontró el vuelo con ID " + id + ". No se pudo eliminar.");
        }

        // Verificar si el vuelo tiene reservas activas
        boolean hasBookings = flightBookingRepository.existsByFlightAndActiveTrue(flight);
        if (hasBookings) {
            return "No se puede eliminar el vuelo, tiene reservas activas.";
        }

        flight.setActive(false);
        flightRepository.save(flight);
        return "Vuelo eliminado con éxito";
    }


    @Override
    public Optional<FlightDTOResponse> findById(Long id) {
        if (id == null) {
            throw new RuntimeException("El campo ID no puede ser nulo. No se pudo buscar.");
        }
        return flightRepository.findByIdAndActiveTrue(id)
                .map(flightMapper::entityToDTO);
    }

    @Override
    public List<FlightDTOResponse> findAvailableFlights(String origin, String destination, LocalDate departureDate, LocalDate returnDate) {
        if (origin == null || destination == null || departureDate == null || returnDate == null || departureDate.isAfter(returnDate)) {
            throw new RuntimeException("Parámetros inválidos: verifique origen, destino y fechas.");
        }

        List<Flight> availableFlights = flightRepository.findByOriginAndDestinationAndDepartureDateAndReturnDateAndActiveTrue(
                origin, destination, departureDate, returnDate
        );

        return availableFlights.stream()
                .map(flightMapper::entityToDTO)
                .toList();
    }
}