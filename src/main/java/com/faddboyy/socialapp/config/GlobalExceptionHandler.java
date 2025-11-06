package com.faddboyy.socialapp.config; // Sesuaikan package Anda

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.faddboyy.socialapp.dto.ErrorDetails;
import com.faddboyy.socialapp.exceptions.UrlNotFoundException;

import java.time.LocalDateTime;

@ControllerAdvice // Ini memberitahu Spring bahwa kelas ini akan menangani exception secara global
public class GlobalExceptionHandler {

    // Handler ini akan dipanggil setiap kali UrlNotFoundException dilempar
    // dari bagian mana pun di aplikasi Anda (controller, service, dll.)
    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleUrlNotFoundException(
            UrlNotFoundException ex, 
            WebRequest request) {
        
        // Buat objek error yang rapi untuk dikirim sebagai JSON
        ErrorDetails errorDetails = new ErrorDetails(
            LocalDateTime.now(),
            ex.getMessage(), // Pesan dari exception (misal: "Comment not found")
            request.getDescription(false) // URL yang diminta
        );

        // Kembalikan ResponseEntity dengan status 404 NOT_FOUND
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    // Anda juga bisa menambahkan handler untuk 'Exception.class' umum
    // untuk menangani error 500 (Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(
            Exception ex, 
            WebRequest request) {
        
        ErrorDetails errorDetails = new ErrorDetails(
            LocalDateTime.now(),
            "Something went wrong", // Jangan ekspos detail error internal
            request.getDescription(false)
        );

        // Kembalikan ResponseEntity dengan status 500 INTERNAL_SERVER_ERROR
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}