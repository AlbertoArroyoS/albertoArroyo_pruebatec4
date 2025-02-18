package com.hackaboss.travelagency.controller;

import com.hackaboss.travelagency.dto.request.FlightBookingRequestDTO;
import com.hackaboss.travelagency.dto.response.FlightBookingResponseDTO;
import com.hackaboss.travelagency.exception.EntityNotDeletableException;
import com.hackaboss.travelagency.exception.EntityNotFoundException;
import com.hackaboss.travelagency.service.IFlightBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agency/flight-booking")
public class FlightBookingController {

    private final IFlightBookingService flightBookingService;

    public FlightBookingController(IFlightBookingService flightBookingService) {
        this.flightBookingService = flightBookingService;
    }


    @PostMapping("/new")
    @Operation(summary = "Crear una reserva de vuelo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva de vuelo creada correctamente"),
            @ApiResponse(responseCode = "404", description = "Vuelo no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe una reserva activa para este vuelo con los mismos pasajeros"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> createFlightBooking(@RequestBody FlightBookingRequestDTO request) {
        try {
            String response = flightBookingService.bookFlight(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (EntityNotDeletableException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado.");
        }
    }

    @GetMapping
    @Operation(summary = "Obtiene el listado de todas las reservas de vuelo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservas de vuelo listadas correctamente"),
            @ApiResponse(responseCode = "400", description = "No se encontraron reservas de vuelo")
    })
    public ResponseEntity<List<FlightBookingResponseDTO>> getAllFlightBookings() {
        List<FlightBookingResponseDTO> activeBookings = flightBookingService.findAll();
        if (activeBookings.isEmpty()) {
            throw new EntityNotFoundException("No se encontraron reservas de vuelo");
        }
        return ResponseEntity.ok(activeBookings);
    }

}
