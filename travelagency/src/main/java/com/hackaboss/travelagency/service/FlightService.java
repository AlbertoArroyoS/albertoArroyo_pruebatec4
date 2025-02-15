package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.FlightDTORequest;
import com.hackaboss.travelagency.dto.response.FlightDTOResponse;
import com.hackaboss.travelagency.mapper.FlightMapper;
import com.hackaboss.travelagency.model.Flight;
import com.hackaboss.travelagency.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
@Service
public class FlightService implements IFlightService {

    private final FlightMapper flightMapper;
    private final FlightRepository flightRepository;

    public FlightService(FlightMapper flightMapper, FlightRepository flightRepository) {
        this.flightMapper = flightMapper;
        this.flightRepository = flightRepository;
    }

    @Override
    public List<FlightDTOResponse> findAll() {
        return flightRepository.findAll().stream()
                .map(flightMapper::entityToDTO)
                .toList();
    }

    @Override
    public String createFlight(FlightDTORequest flightDTORequest) {
        Flight flight = flightMapper.requestToEntity(flightDTORequest);
        flightRepository.save(flight);
        return "Vuelo creado con éxito";
    }


    @Override
    public List<FlightDTOResponse> findAvailableFlights(String origin, String destination, LocalDate departureDate, LocalDate returnDate) {
        // Validación de parámetros
        if (origin == null || destination == null || departureDate == null || returnDate == null || departureDate.isAfter(returnDate)) {
            throw new IllegalArgumentException("Parámetros inválidos: verifique origen, destino y fechas.");
        }

        // Se consulta el repositorio para obtener los vuelos disponibles
        List<Flight> availableFlights = flightRepository.findByOriginAndDestinationAndDepartureDateAndReturnDateAndActiveTrue(
                origin, destination, departureDate, returnDate
        );

        // Se convierte cada entidad Flight a FlightDTOResponse usando el mapper configurado
        return availableFlights.stream()
                .map(flight -> flightMapper.entityToDTO(flight))
                .toList();
    }
}

