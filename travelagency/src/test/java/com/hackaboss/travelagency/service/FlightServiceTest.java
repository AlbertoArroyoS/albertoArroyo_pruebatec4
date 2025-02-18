package com.hackaboss.travelagency.service;

import com.hackaboss.travelagency.dto.request.FlightDTORequest;
import com.hackaboss.travelagency.dto.response.FlightDTOResponse;
import com.hackaboss.travelagency.exception.EntityExistsException;
import com.hackaboss.travelagency.exception.EntityNotFoundException;
import com.hackaboss.travelagency.exception.InvalidDataException;
import com.hackaboss.travelagency.mapper.FlightMapper;
import com.hackaboss.travelagency.model.Flight;
import com.hackaboss.travelagency.model.FlightBooking;
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

    @Mock
    private FlightMapper flightMapper;

    @InjectMocks
    private FlightService flightService;

    @BeforeEach
    public void setup() {
        // Con @ExtendWith(MockitoExtension.class) no es necesario llamar a initMocks manualmente.
        reset(flightRepository, flightMapper);
    }

    @Test
    @DisplayName("Test para listar todos los vuelos activos")
    void testFindAll() {
        Flight flight1 = new Flight();
        Flight flight2 = new Flight();
        List<Flight> flights = List.of(flight1, flight2);

        when(flightRepository.findByActiveTrue()).thenReturn(flights);
        when(flightMapper.entityToDTO(any(Flight.class))).thenReturn(new FlightDTOResponse());

        List<FlightDTOResponse> result = flightService.findAll();

        assertEquals(flights.size(), result.size());
    }

    @Test
    @DisplayName("Test para crear un nuevo vuelo")
    void testCreateFlight() {
        FlightDTORequest dtoRequest = new FlightDTORequest();
        dtoRequest.setFlightNumber("ABC123");
        // Completar otros campos de dtoRequest según sea necesario

        Flight flight = new Flight();
        flight.setFlightNumber("ABC123");

        when(flightRepository.findByFlightNumber("ABC123")).thenReturn(Optional.empty());
        when(flightMapper.requestToEntity(dtoRequest)).thenReturn(flight);
        when(flightRepository.save(flight)).thenReturn(flight);

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
    @DisplayName("Test para eliminar un vuelo con reservas activas")
    void testDeleteFlightWithBookings() {
        Long id = 1L;
        Flight flight = new Flight();
        flight.setId(id);
        flight.setActive(true);
        // Simulamos una reserva activa
        FlightBooking booking = new FlightBooking();
        booking.setFlight(flight);
        flight.setListFlightBookings(List.of(booking));

        when(flightRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.of(flight));

        String result = flightService.deleteFlight(id);
        assertEquals("No se puede eliminar el vuelo, tiene reservas activas.", result);
    }

    @Test
    @DisplayName("Test para buscar un vuelo por ID")
    void testFindById() {
        Long id = 1L;
        Flight flight = new Flight();
        flight.setId(id);
        FlightDTOResponse dtoResponse = new FlightDTOResponse();

        when(flightRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.of(flight));
        when(flightMapper.entityToDTO(flight)).thenReturn(dtoResponse);

        Optional<FlightDTOResponse> result = flightService.findById(id);
        assertTrue(result.isPresent());
        assertEquals(dtoResponse, result.get());
    }

    @Test
    @DisplayName("Test para buscar un vuelo por ID que no existe")
    void testFindByIdNotFound() {
        Long id = 1L;
        when(flightRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.empty());
        Optional<FlightDTOResponse> result = flightService.findById(id);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test para buscar vuelos disponibles")
    void testFindAvailableFlights() {
        String origin = "Madrid";
        String destination = "Paris";
        LocalDate departureDate = LocalDate.of(2025, 5, 1);
        LocalDate returnDate = LocalDate.of(2025, 5, 10);

        Flight flight = new Flight();
        flight.setOrigin(origin);
        flight.setDestination(destination);
        flight.setDepartureDate(departureDate);
        flight.setReturnDate(returnDate);
        flight.setActive(true);

        List<Flight> flights = List.of(flight);
        FlightDTOResponse dtoResponse = new FlightDTOResponse();

        when(flightRepository.findByOriginAndDestinationAndDepartureDateAndReturnDateAndActiveTrue(
                origin, destination, departureDate, returnDate)).thenReturn(flights);
        when(flightMapper.entityToDTO(flight)).thenReturn(dtoResponse);

        List<FlightDTOResponse> result = flightService.findAvailableFlights(origin, destination, departureDate, returnDate);
        assertEquals(1, result.size());
        assertEquals(dtoResponse, result.get(0));
    }

    @Test
    @DisplayName("Test para buscar vuelos disponibles con parámetros inválidos")
    void testFindAvailableFlightsInvalidParams() {
        String origin = "Madrid";
        String destination = "Paris";
        // Fechas inválidas: fecha de salida posterior a fecha de regreso
        LocalDate departureDate = LocalDate.of(2025, 5, 10);
        LocalDate returnDate = LocalDate.of(2025, 5, 1);

        assertThrows(InvalidDataException.class, () ->
                flightService.findAvailableFlights(origin, destination, departureDate, returnDate));
    }

    @Test
    @DisplayName("Test para buscar vuelos disponibles cuando no hay resultados")
    void testFindAvailableFlightsNotFound() {
        String origin = "Madrid";
        String destination = "Paris";
        LocalDate departureDate = LocalDate.of(2025, 5, 1);
        LocalDate returnDate = LocalDate.of(2025, 5, 10);

        when(flightRepository.findByOriginAndDestinationAndDepartureDateAndReturnDateAndActiveTrue(
                origin, destination, departureDate, returnDate)).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () ->
                flightService.findAvailableFlights(origin, destination, departureDate, returnDate));
    }
}
