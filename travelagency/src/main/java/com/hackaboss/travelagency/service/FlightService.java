package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.FlightDTORequest;
import com.hackaboss.travelagency.dto.response.FlightDTOResponse;
import com.hackaboss.travelagency.mapper.FlightMapper;
import com.hackaboss.travelagency.model.Flight;
import com.hackaboss.travelagency.repository.FlightRepository;
import org.springframework.stereotype.Service;

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
}
