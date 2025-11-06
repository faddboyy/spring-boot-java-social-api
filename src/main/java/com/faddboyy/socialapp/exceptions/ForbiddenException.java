package com.faddboyy.socialapp.exceptions;

/**
 * Exception yang dilempar ketika user mencoba aksi yang tidak diizinkan.
 * Akan ditangani oleh RestExceptionHandler untuk mengembalikan 403 FORBIDDEN.
 */
public class ForbiddenException extends RuntimeException {
    
    public ForbiddenException(String message) {
        super(message);
    }
}
