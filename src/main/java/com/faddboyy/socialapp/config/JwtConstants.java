package com.faddboyy.socialapp.config;

public class JwtConstants {
    
    /**
     * Nama header HTTP yang membawa token.
     */
    public static final String JWT_HEADER = "Authorization";

    /**
     * Awalan (prefix) yang digunakan sebelum token di header.
     * (Contoh: "Bearer <token>")
     */
    public static final String BEARER_PREFIX = "Bearer ";
    
}
