package com.faddboyy.socialapp.controllers;

import com.faddboyy.socialapp.config.JwtProvider;
import com.faddboyy.socialapp.response.AuthResponse;
import com.faddboyy.socialapp.request.LoginRequest;
import com.faddboyy.socialapp.dto.UserDto;
import com.faddboyy.socialapp.entities.User;
import com.faddboyy.socialapp.services.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    // 1. Constructor diperbarui untuk meng-inject semua service yang dibutuhkan
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    /**
     * Endpoint untuk registrasi user baru.
     * Path lengkap: POST /auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody User reqUser) {
        UserDto savedUserDto = userService.registerUser(reqUser);
        return new ResponseEntity<>(savedUserDto, HttpStatus.CREATED);
    }

    /**
     * 2. [ENDPOINT BARU] Endpoint untuk login user.
     * Path lengkap: POST /auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest req) {
        try {
            // 3. Autentikasi user
            Authentication authentication = authenticate(req.getEmail(), req.getPassword());
            
            // 4. Jika sukses, set di SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 5. Buat token JWT
            String token = jwtProvider.generateToken(authentication);
            
            // 6. Ambil data UserDto
            UserDto userDto = userService.findUserByEmail(authentication.getName());

            // 7. Kirim token dan UserDto sebagai respons
            AuthResponse authResponse = new AuthResponse(token, userDto);
            
            return new ResponseEntity<>(authResponse, HttpStatus.OK);

        } catch (BadCredentialsException e) {
            // 8. Tangani jika email atau password salah
            return new ResponseEntity<>(
                Map.of("message", "Invalid email or password"), 
                HttpStatus.UNAUTHORIZED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                Map.of("message", e.getMessage()), 
                HttpStatus.BAD_REQUEST
            );
        }
    }
    
    /**
     * 3. [ENDPOINT BARU] Endpoint untuk logout.
     * Path lengkap: POST /auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        // Untuk JWT, logout ditangani di sisi client (hapus token).
        // Server hanya perlu mengirimkan konfirmasi.
        return ResponseEntity.ok(
            Map.of("message", "Logout successful. Please clear your token on the client-side.")
        );
    }

    /**
     * Helper method untuk memanggil AuthenticationManager
     */
    private Authentication authenticate(String email, String password) {
        // Objek ini akan digunakan oleh AuthenticationManager untuk mencari user
        // dan memvalidasi password menggunakan UserDetailsService & PasswordEncoder
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
        return authenticationManager.authenticate(token);
    }
}
