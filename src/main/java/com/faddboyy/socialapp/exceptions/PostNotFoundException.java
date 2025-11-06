package com.faddboyy.socialapp.exceptions;

/**
 * Exception yang dilempar ketika post tidak ditemukan.
 * Akan ditangani oleh RestExceptionHandler untuk mengembalikan 404 NOT_FOUND.
 */
public class PostNotFoundException extends RuntimeException {
    
    public PostNotFoundException(String message) {
        super(message);
    }
}
