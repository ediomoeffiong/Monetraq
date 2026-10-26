package com.trasexp.presentation.exception;

import com.trasexp.presentation.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("Validation error: {}", ex.getMessage());
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        ErrorResponse error = new ErrorResponse("Validation Failed", details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime exception occurred: {}", ex.getMessage(), ex);
        String message = ex.getMessage();
        if (message != null && message.contains("not found")) {
            ErrorResponse error = new ErrorResponse("Resource Not Found", List.of(message));
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } else {
            ErrorResponse error = new ErrorResponse("Bad Request", List.of(message));
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("Data integrity violation: {}", ex.getMessage());
        String message = "The operation could not be completed. This is usually due to an amount exceeding the maximum allowed limit or a duplicate entry.";
        if (ex.getMessage() != null && ex.getMessage().contains("Value too long for column \"AMOUNT")) {
            message = "The amount entered exceeds the maximum allowed limit.";
        }
        ErrorResponse error = new ErrorResponse("Data Error", List.of(message));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("Illegal argument: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse("Bad Request", List.of(ex.getMessage()));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Handle missing required query params like userId
    @ExceptionHandler(org.springframework.web.bind.MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(
            org.springframework.web.bind.MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        log.warn("Missing request parameter: {}", name);
        ErrorResponse error = new ErrorResponse("Missing Parameter",
                List.of("Required parameter '" + name + "' is missing"));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Handle JSON parse errors (e.g., invalid enum values)
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParse(
            org.springframework.http.converter.HttpMessageNotReadableException ex) {
        log.warn("JSON parse error: {}", ex.getMessage());
        String message = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        ErrorResponse error = new ErrorResponse("Malformed Request", List.of(message));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        log.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        ErrorResponse error = new ErrorResponse("Internal Server Error",
                List.of("An unexpected error occurred: " + ex.getMessage()));
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}