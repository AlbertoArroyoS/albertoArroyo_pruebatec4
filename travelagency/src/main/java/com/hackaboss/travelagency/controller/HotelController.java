package com.hackaboss.travelagency.controller;

import com.hackaboss.travelagency.dto.request.HotelDTORequest;
import com.hackaboss.travelagency.dto.response.HotelDTOResponse;
import com.hackaboss.travelagency.exception.EntityExistsException;
import com.hackaboss.travelagency.exception.EntityNotFoundException;
import com.hackaboss.travelagency.service.HotelService;
import com.hackaboss.travelagency.service.IHotelService;
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
@RequestMapping("/agency/hotels")
public class HotelController {

    private final IHotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los hoteles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hoteles listados correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron hoteles"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<HotelDTOResponse>> getAllHotels() {
        try {
            return ResponseEntity.ok(hotelService.findAll());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/new")
    @Operation(summary = "Crear un nuevo hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Hotel creado correctamente"),
            @ApiResponse(responseCode = "400", description = "No se pudo crear el hotel"),
            @ApiResponse(responseCode = "409", description = "El hotel ya existe")
    })
    public ResponseEntity<String> createHotel(@Valid @RequestBody HotelDTORequest hotelDTORequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.createHotel(hotelDTORequest));
        } catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/edit/{id}")
    @Operation(summary = "Actualizar un hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "No se pudo actualizar el hotel")
    })
    public ResponseEntity<String> updateHotel(@PathVariable Long id, @Valid @RequestBody HotelDTORequest hotelDTORequest) {
        try {
            return ResponseEntity.ok(hotelService.updateHotel(id, hotelDTORequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Eliminar un hotel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel eliminado correctamente"),
            @ApiResponse(responseCode = "400", description = "No se pudo eliminar el hotel")
    })
    public ResponseEntity<String> deleteHotel(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(hotelService.deleteHotel(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar un hotel por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Hotel encontrado correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró el hotel")
    })
    public ResponseEntity<HotelDTOResponse> getHotelById(@PathVariable Long id) {
        HotelDTOResponse hotel = hotelService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró el hotel con id: " + id));
        return ResponseEntity.ok(hotel);
    }


    @GetMapping("/rooms")
    @Operation(summary = "Buscar habitaciones disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Habitaciones disponibles listadas correctamente"),
            @ApiResponse(responseCode = "404", description = "No se encontraron habitaciones disponibles en esas fechas y destino")
    })
    public ResponseEntity<List<HotelDTOResponse>> getAvailableRooms(
            @RequestParam("dateFrom") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateFrom,
            @RequestParam("dateTo") @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateTo,
            @RequestParam("destination") String destination) {

        List<HotelDTOResponse> availableHotels = hotelService.findAvailableRooms(destination, dateFrom, dateTo);
        if (availableHotels.isEmpty()) {
            throw new EntityNotFoundException("No hay hoteles disponibles en esas fechas y destino.");
        }
        return ResponseEntity.ok(availableHotels);
    }
}