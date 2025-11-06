package com.faddboyy.socialapp.exceptions;

/**
 * Exception kustom ini akan dilempar ketika mencoba mendaftar
 * dengan email yang sudah ada di database.
 * Ini adalah RuntimeException (unchecked) agar kita tidak perlu 'throws' di signature.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
