package com.faddboyy.socialapp.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    // 1. Inject nilai dari application.properties
    @Value("${app.jwt.secret-key}")
    private String secretKeyString;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpiration;

    private SecretKey key;

    /**
     * 2. Inisialisasi 'key' satu kali setelah properti di-inject.
     * Ini lebih efisien daripada membuatnya berulang kali.
     */
    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    public String generateToken(Authentication auth) {
        
        String email = auth.getName();
        
        // (Opsional) Kumpulkan roles/authorities user
        String authorities = auth.getAuthorities().stream()
                                .map(a -> a.getAuthority())
                                .collect(Collectors.joining(","));

        Date now = new Date();
        // 3. Gunakan nilai expiration yang di-inject
        Date expirationDate = new Date(now.getTime() + jwtExpiration); 
        
        String jwt = Jwts.builder()
                .setIssuer("SocialApp")
                .setSubject(email)
                
                // 4. Simpan 'email' dan 'authorities' sebagai 'claims'
                .claim("email", email)
                .claim("authorities", authorities)
                
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key) // Gunakan key yang sudah diinisialisasi
                .compact();
        
        return jwt;
    }
}
