package com.faddboyy.socialapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Opsi 1: Menggunakan @ResponseStatus (Sederhana)
// @ResponseStatus(value = HttpStatus.NOT_FOUND) // <-- Ini akan otomatis menghasilkan 404

// Opsi 2: Biarkan GlobalExceptionHandler yang menangani (Lebih Fleksibel, ini yang kita pakai di Langkah 2)
public class UrlNotFoundException extends RuntimeException {

    public UrlNotFoundException(String message) {
        super(message);
    }
}