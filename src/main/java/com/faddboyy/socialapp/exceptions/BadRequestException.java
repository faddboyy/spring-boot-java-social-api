package com.faddboyy.socialapp.exceptions;

/**
 * Exception yang dilempar untuk input yang buruk dari client.
 * Akan ditangani oleh RestExceptionHandler untuk mengembalikan 400 BAD_REQUEST.
 */
public class BadRequestException extends RuntimeException {
    
    public BadRequestException(String message) {
        super(message);
    }
}
