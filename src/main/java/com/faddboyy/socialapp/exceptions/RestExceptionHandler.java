package com.faddboyy.socialapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Kelas @RestControllerAdvice ini akan menangani exception secara global
 * untuk semua @RestController.
 */
@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * Menangani EmailAlreadyExistsException.
     * Kapanpun exception ini dilempar, metode ini akan berjalan.
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex, WebRequest request) {
        
        // Buat body respons error yang rapi
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value()); // 400
        body.put("error", "Bad Request");
        body.put("message", ex.getMessage()); // Pesan dari service: "Email 'x' is already taken."
        body.put("path", request.getDescription(false).replace("uri=", ""));
        
        // Kembalikan ResponseEntity dengan status 400 BAD_REQUEST
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    /**
     * Menangani semua RuntimeException lain (yang tidak terduga).
     * Ini akan mengubah error 500 generik menjadi JSON yang rapi.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleGenericRuntimeException(
            RuntimeException ex, WebRequest request) {
        
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value()); // 500
        body.put("error", "Internal Server Error");
        body.put("message", "An unexpected error occurred: " + ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        // Kembalikan ResponseEntity dengan status 500
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
