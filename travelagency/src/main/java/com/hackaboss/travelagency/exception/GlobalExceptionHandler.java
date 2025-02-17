package com.hackaboss.travelagency.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //constante para el error
    private static final String ERROR = "error";

    // Maneja errores de validación (como @NotBlank, @NotNull, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errors);
    }

    // Captura IllegalArgumentException (como cuando roomType es incorrecto)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("roomType", ex.getMessage()); // Mensaje claro sobre el error
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // Captura DatosInvalidosException (cuando los datos son nulos o incorrectos)
    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<Map<String, String>> handleDatosInvalidosException(InvalidDataException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(ERROR, ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    // Captura VueloNoEncontradoException (cuando un vuelo no existe)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleVueloNoEncontradoException(EntityNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    // Captura VueloNoEliminableException (cuando un vuelo tiene reservas activas y no puede eliminarse)
    @ExceptionHandler(EntityNotDeletableException.class)
    public ResponseEntity<Map<String, String>> handleVueloNoEliminableException(EntityNotDeletableException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // Captura EntityExistsException (cuando ya existe una entidad con el mismo valor)
    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<Map<String, String>> handleEntityExistsException(EntityExistsException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}

