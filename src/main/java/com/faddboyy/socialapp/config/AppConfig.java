package com.faddboyy.socialapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse; // Import baru
import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class AppConfig {

    private final JwtValidator jwtValidator;
    
    public AppConfig(JwtValidator jwtValidator) {
        this.jwtValidator = jwtValidator;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(Authorize -> Authorize
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtValidator, BasicAuthenticationFilter.class)
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // ======================================================
            // [PERUBAHAN] TAMBAHKAN BLOK INI
            // ======================================================
            .exceptionHandling(ex -> ex
                /**
                 * 1. AuthenticationEntryPoint
                 * Ini akan dipicu jika user TIDAK TERAUTENTIKASI
                 * (misal: tidak mengirim token) dan mencoba mengakses /api/**
                 */
                .authenticationEntryPoint((request, response, authException) -> 
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                )
                /**
                 * 2. AccessDeniedHandler
                 * Ini akan dipicu jika user SUDAH TERAUTENTIKASI
                 * (token valid) tapi TIDAK PUNYA IZIN
                 * (misal: role USER mencoba akses endpoint ADMIN)
                 */
                .accessDeniedHandler((request, response, accessDeniedException) -> 
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden")
                )
            );
            // ======================================================

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration cfg = new CorsConfiguration();
                
                // 1. Izinkan dari mana saja (Sudah benar)
                cfg.setAllowedOrigins(Arrays.asList("*")); 
                
                // 2. Izinkan semua method (Sudah benar)
                cfg.setAllowedMethods(Collections.singletonList("*")); 
                
                // 3. Izinkan semua header (Sudah benar)
                cfg.setAllowedHeaders(Collections.singletonList("*")); 
                
                // 4. Izinkan frontend membaca header 'Authorization' (Sudah benar)
                cfg.setExposedHeaders(Arrays.asList(JwtConstants.JWT_HEADER));
                
                // 5. [PERUBAHAN PENTING]
                //    Ubah ini menjadi 'false' agar AllowedOrigins("*") bisa berfungsi.
                cfg.setAllowCredentials(false); // <--- UBAH INI DARI 'true'
                
                cfg.setMaxAge(3600L);
                return cfg;
            }
        };
    }

    // Bean PasswordEncoder (Sudah Benar)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // Bean AuthenticationManager (Sudah Benar)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}

