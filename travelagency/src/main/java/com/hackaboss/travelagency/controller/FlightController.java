package com.hackaboss.travelagency.controller;

import com.hackaboss.travelagency.dto.request.FlightDTORequest;
import com.hackaboss.travelagency.dto.response.FlightDTOResponse;
import com.hackaboss.travelagency.exception.EntityNotFoundException;
import com.hackaboss.travelagency.exception.InvalidDataException;
import com.hackaboss.travelagency.service.IFlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/agency/flights")
public class FlightController {

    private final IFlightService flightService;

    public FlightController(IFlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    @Operation(summary = "Obtiene el listado de todos los vuelos registrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vuelos listados correctamente"),
            @ApiResponse(responseCode = "400", description = "No se encontraron vuelos")
    })
    public ResponseEntity<List<FlightDTOResponse>> getAllFlights() {
        try {
            return ResponseEntity.ok(flightService.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/new")
    @Operation(summary = "Crea un nuevo vuelo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vuelo creado correctamente"),
            @ApiResponse(responseCode = "400", description = "No se pudo crear el vuelo")
    })
    public ResponseEntity<String> createFlight(@Valid @RequestBody FlightDTORequest flightDTORequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(flightService.createFlight(flightDTORequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/edit/{id}")
    @Operation(summary = "Actualiza un vuelo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vuelo actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "No se pudo actualizar el vuelo")
    })
    public ResponseEntity<String> updateFlight(@PathVariable Long id, @Valid @RequestBody FlightDTORequest flightDTORequest) {
        try {
            return ResponseEntity.ok(flightService.updateFlight(id, flightDTORequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Elimina un vuelo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vuelo eliminado correctamente"),
            @ApiResponse(responseCode = "400", description = "No se pudo eliminar el vuelo")
    })
    public ResponseEntity<String> deleteFlight(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(flightService.deleteFlight(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un vuelo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vuelo encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró el vuelo")
    })
    public ResponseEntity<FlightDTOResponse> findFlightById(@PathVariable Long id) {
        Optional<FlightDTOResponse> flightOpt = flightService.findById(id);
        return flightOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("/available")
    @Operation(summary = "Obtiene el listado de vuelos disponibles, por origen, destino y rango de fechas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vuelos disponibles listados correctamente"),
            @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
            @ApiResponse(responseCode = "404", description = "No se encontraron vuelos disponibles")
    })
    public ResponseEntity<List<FlightDTOResponse>> findAvailableFlights(
            @RequestParam("dateFrom") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateFrom,
            @RequestParam("dateTo") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateTo,
            @RequestParam("origin") String origin,
            @RequestParam("destination") String destination) {
        try {
            List<FlightDTOResponse> flights = flightService.findAvailableFlights(origin, destination, dateFrom, dateTo);
            return ResponseEntity.ok(flights);
        } catch (InvalidDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // 400 BAD REQUEST si los parámetros son inválidos
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 NOT FOUND si no hay vuelos disponibles
        }
    }

}