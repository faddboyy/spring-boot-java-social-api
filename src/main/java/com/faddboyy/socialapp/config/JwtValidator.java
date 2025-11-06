package com.faddboyy.socialapp.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

@Component
public class JwtValidator extends OncePerRequestFilter {

    // Kita tidak lagi butuh UserDetailsService di sini
    // karena semua data (email, roles) sudah ada di token.
    private final SecretKey key;
    private static final Logger logger = LoggerFactory.getLogger(JwtValidator.class);

    /**
     * 1. Inject secret-key dari properties dan inisialisasi 'key' satu kali.
     */
    public JwtValidator(@Value("${app.jwt.secret-key}") String secretKeyString) {
        this.key = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String jwtHeader = request.getHeader(JwtConstants.JWT_HEADER);
        
        if(jwtHeader != null && jwtHeader.startsWith(JwtConstants.BEARER_PREFIX)) {
            
            // 2. Gunakan konstanta untuk mengekstrak token
            String jwt = jwtHeader.substring(JwtConstants.BEARER_PREFIX.length());
            
            try {
                Claims claims = Jwts.parserBuilder()
                                    .setSigningKey(key)
                                    .build()
                                    .parseClaimsJws(jwt)
                                    .getBody();
                
                String email = String.valueOf(claims.get("email"));
                String authoritiesString = String.valueOf(claims.get("authorities"));

                // Konversi string authorities (misal: "USER,ADMIN") kembali ke List
                List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesString);
                
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        email, // (Principal)
                        null, 
                        authorities
                );
                    
                // 3. SET user sebagai terautentikasi
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
            } catch (ExpiredJwtException e) {
                logger.warn("JWT token has expired: {}", e.getMessage());
                sendError(response, "Token expired");
                return;
            } catch (MalformedJwtException | SignatureException e) {
                logger.warn("Invalid JWT token: {}", e.getMessage());
                sendError(response, "Invalid token");
                return;
            } catch (Exception e) {
                logger.error("Error validating JWT token: {}", e.getMessage());
                sendError(response, "Error validating token");
                return;
            }
        }
        
        filterChain.doFilter(request, response);
    }

    /**
     * 4. [SOLUSI] Metode ini akan MELEWATKAN filter
     * jika path request diawali dengan "/auth/".
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.startsWith("/auth/");
    }

    // Helper untuk mengirim response error yang konsisten
    private void sendError(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        // Mengirim error dalam format JSON
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
