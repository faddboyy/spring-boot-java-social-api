package com.faddboyy.socialapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception ini akan dilempar ketika user tidak ditemukan di database.
 * Anotasi @ResponseStatus(HttpStatus.NOT_FOUND) bisa digunakan,
 * tapi kita akan menanganinya di RestExceptionHandler agar lebih konsisten.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}
