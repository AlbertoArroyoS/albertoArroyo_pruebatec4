package com.hackaboss.travelagency.controller;

import com.hackaboss.travelagency.dto.request.HotelBookingDTORequest;
import com.hackaboss.travelagency.dto.response.HotelBookingDTOResponse;
import com.hackaboss.travelagency.exception.EntityNotDeletableException;
import com.hackaboss.travelagency.exception.EntityNotFoundException;
import com.hackaboss.travelagency.exception.InvalidDataException;
import com.hackaboss.travelagency.service.IHotelBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agency/room-booking")
public class HotelBookingController {

    private final IHotelBookingService hotelBookingService;

    public HotelBookingController(IHotelBookingService hotelBookingService) {
        this.hotelBookingService = hotelBookingService;
    }


    // GET /agency/flights - Obtiene el listado de todos los vuelos registrados
    @GetMapping
    @Operation(summary = "Obtiene el listado de todas las reservas de hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservas de hotel listadas correctamente"),
            @ApiResponse(responseCode = "400", description = "No se encontraron reservas de hotel")
    })
    public ResponseEntity<List<HotelBookingDTOResponse>> getAllHotelBookings() {
        try {
            return ResponseEntity.ok(hotelBookingService.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    //Post
    @PostMapping("/new")
    @Operation(summary = "Crear una reserva de hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva de hotel creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de reserva inválidos"),
            @ApiResponse(responseCode = "404", description = "Hotel no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe una reserva activa para este hotel en las mismas fechas"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<String> createHotelBooking(@RequestBody HotelBookingDTORequest request) {
        try {
            String response = hotelBookingService.createHotelBooking(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidDataException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (EntityNotDeletableException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado.");
        }
    }

}


