package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.FlightDTORequest;
import com.hackaboss.travelagency.dto.response.FlightDTOResponse;
import com.hackaboss.travelagency.exception.EntityExistsException;
import com.hackaboss.travelagency.exception.EntityNotDeletableException;
import com.hackaboss.travelagency.exception.InvalidDataException;
import com.hackaboss.travelagency.exception.EntityNotFoundException;
import com.hackaboss.travelagency.model.Flight;
import com.hackaboss.travelagency.repository.FlightRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService implements IFlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public List<FlightDTOResponse> findAll() {
        return flightRepository.findByActiveTrue().stream()
                .map(this::entityToDTO)
                .toList();
    }

    @Override
    @Transactional
    public String createFlight(FlightDTORequest flightDTORequest) {
        if (flightDTORequest == null) {
            throw new InvalidDataException("Datos del vuelo no pueden ser nulos. No se pudo crear.");
        }
        Optional<Flight> existingFlight = flightRepository.findByFlightNumber(flightDTORequest.getFlightNumber());
        if (existingFlight.isPresent()) {
            throw new EntityExistsException("El vuelo con número '" + flightDTORequest.getFlightNumber() + "' ya existe.");
        }
        Flight flight = requestToEntity(flightDTORequest);
        flightRepository.save(flight);
        return "Vuelo creado con éxito";
    }

    @Override
    @Transactional
    public String updateFlight(Long id, FlightDTORequest flightDTORequest) {
        if (id == null || flightDTORequest == null) {
            throw new InvalidDataException("Datos inválidos para actualizar el vuelo. No se pudo actualizar.");
        }
        Flight flight = flightRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el vuelo con ID " + id + ". No se pudo actualizar."));

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
            throw new InvalidDataException("ID del vuelo no puede ser nulo. No se pudo eliminar.");
        }
        Flight flight = flightRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el vuelo con ID " + id + ". No se pudo eliminar."));

        boolean hasBookings = flight.getListFlightBookings().stream()
                .anyMatch(booking -> booking.getFlight().getId().equals(id));
        if (hasBookings) {
            throw new EntityNotDeletableException("No se puede eliminar el vuelo, tiene reservas activas.");
        }

        flight.setActive(false);
        flightRepository.save(flight);
        return "Vuelo eliminado con éxito";
    }

    @Override
    public Optional<FlightDTOResponse> findById(Long id) {
        if (id == null) {
            throw new InvalidDataException("El campo ID no puede ser nulo. No se pudo buscar.");
        }
        return flightRepository.findByIdAndActiveTrue(id)
                .map(this::entityToDTO);
    }

    @Override
    public List<FlightDTOResponse> findAvailableFlights(String origin, String destination, LocalDate departureDate, LocalDate returnDate) {
        if (origin == null || destination == null || departureDate == null || returnDate == null || departureDate.isAfter(returnDate)) {
            throw new InvalidDataException("Parámetros inválidos: verifique origen, destino y fechas.");
        }
        List<Flight> availableFlights = flightRepository.findByOriginAndDestinationAndDepartureDateAndReturnDateAndActiveTrue(
                origin, destination, departureDate, returnDate);
        if (availableFlights.isEmpty()) {
            throw new EntityNotFoundException("No hay vuelos disponibles.");
        }
        return availableFlights.stream()
                .map(this::entityToDTO)
                .toList();
    }

    @Override
    public Flight findActiveFlight(FlightDTORequest flightDTO) {
        if (flightDTO.getId() != null) {
            return flightRepository.findByIdAndActiveTrue(flightDTO.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Vuelo no encontrado"));
        } else {
            return flightRepository.findByFlightNumberAndActiveTrue(flightDTO.getFlightNumber())
                    .orElseThrow(() -> new EntityNotFoundException("Vuelo no encontrado"));
        }
    }

    @Override
    public Flight save(Flight flight) {
        return flightRepository.save(flight);
    }

    // Métodos de conversión
    private FlightDTOResponse entityToDTO(Flight flight) {
        return FlightDTOResponse.builder()
                .flightNumber(flight.getFlightNumber())
                .origin(flight.getOrigin())
                .destination(flight.getDestination())
                .seatType(flight.getSeatType())
                .ratePerPerson(flight.getRatePerPerson())
                .departureDate(flight.getDepartureDate())
                .returnDate(flight.getReturnDate())
                .build();
    }

    private Flight requestToEntity(FlightDTORequest flightDTORequest) {
        return Flight.builder()
                .flightNumber(flightDTORequest.getFlightNumber())
                .origin(flightDTORequest.getOrigin())
                .destination(flightDTORequest.getDestination())
                .seatType(flightDTORequest.getSeatType())
                .ratePerPerson(flightDTORequest.getRatePerPerson())
                .departureDate(flightDTORequest.getDepartureDate())
                .returnDate(flightDTORequest.getReturnDate())
                .build();
    }
}
