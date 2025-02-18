package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.FlightDTORequest;
import com.hackaboss.travelagency.dto.response.FlightDTOResponse;
import com.hackaboss.travelagency.exception.EntityExistsException;
import com.hackaboss.travelagency.exception.EntityNotFoundException;
import com.hackaboss.travelagency.model.Flight;
import com.hackaboss.travelagency.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    @BeforeEach
    public void setup() {
        reset(flightRepository);
    }

    @Test
    @DisplayName("Test para listar todos los vuelos activos")
    void testFindAll() {
        Flight flight1 = new Flight();
        Flight flight2 = new Flight();
        List<Flight> flights = List.of(flight1, flight2);

        when(flightRepository.findByActiveTrue()).thenReturn(flights);

        List<FlightDTOResponse> result = flightService.findAll();

        assertEquals(flights.size(), result.size());
    }

    @Test
    @DisplayName("Test para crear un nuevo vuelo")
    void testCreateFlight() {
        FlightDTORequest dtoRequest = new FlightDTORequest();
        dtoRequest.setFlightNumber("ABC123");

        when(flightRepository.findByFlightNumber("ABC123")).thenReturn(Optional.empty());
        when(flightRepository.save(any(Flight.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String result = flightService.createFlight(dtoRequest);
        assertEquals("Vuelo creado con éxito", result);
    }

    @Test
    @DisplayName("Test para crear un vuelo con número ya existente")
    void testCreateFlightExisting() {
        FlightDTORequest dtoRequest = new FlightDTORequest();
        dtoRequest.setFlightNumber("ABC123");

        Flight flight = new Flight();
        flight.setFlightNumber("ABC123");

        when(flightRepository.findByFlightNumber("ABC123")).thenReturn(Optional.of(flight));

        assertThrows(EntityExistsException.class, () -> flightService.createFlight(dtoRequest));
    }

    @Test
    @DisplayName("Test para actualizar un vuelo existente")
    void testUpdateFlight() {
        Long id = 1L;
        FlightDTORequest dtoRequest = new FlightDTORequest();
        dtoRequest.setFlightNumber("XYZ789");
        dtoRequest.setOrigin("CiudadA");
        dtoRequest.setDestination("CiudadB");
        dtoRequest.setSeatType("Economy");
        dtoRequest.setRatePerPerson(150.0);
        dtoRequest.setDepartureDate(LocalDate.of(2025, 6, 1));
        dtoRequest.setReturnDate(LocalDate.of(2025, 6, 10));

        Flight flight = new Flight();
        flight.setId(id);

        when(flightRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.of(flight));
        when(flightRepository.save(any(Flight.class))).thenReturn(flight);

        String result = flightService.updateFlight(id, dtoRequest);
        assertEquals("Vuelo actualizado con éxito", result);
    }

    @Test
    @DisplayName("Test para actualizar un vuelo que no existe")
    void testUpdateFlightNotFound() {
        Long id = 1L;
        FlightDTORequest dtoRequest = new FlightDTORequest();

        when(flightRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> flightService.updateFlight(id, dtoRequest));
    }

    @Test
    @DisplayName("Test para eliminar un vuelo exitosamente")
    void testDeleteFlight() {
        Long id = 1L;
        Flight flight = new Flight();
        flight.setId(id);
        flight.setActive(true);
        flight.setListFlightBookings(Collections.emptyList());

        when(flightRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.of(flight));
        when(flightRepository.save(flight)).thenReturn(flight);

        String result = flightService.deleteFlight(id);
        assertEquals("Vuelo eliminado con éxito", result);
    }

    @Test
    @DisplayName("Test para eliminar un vuelo que no existe")
    void testDeleteFlightNotFound() {
        Long id = 1L;
        when(flightRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> flightService.deleteFlight(id));
    }

    @Test
    @DisplayName("Test para buscar un vuelo por ID")
    void testFindById() {
        Long id = 1L;
        Flight flight = new Flight();
        flight.setId(id);

        when(flightRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.of(flight));

        Optional<FlightDTOResponse> result = flightService.findById(id);
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("Test para buscar un vuelo por ID que no existe")
    void testFindByIdNotFound() {
        Long id = 1L;
        when(flightRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.empty());
        Optional<FlightDTOResponse> result = flightService.findById(id);
        assertTrue(result.isEmpty());
    }
}

