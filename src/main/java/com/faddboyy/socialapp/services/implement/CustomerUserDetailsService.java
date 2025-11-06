package com.faddboyy.socialapp.services.implement; // 1. Dipindah ke package 'impl'

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
// 2. Import baru untuk menambahkan authority
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.faddboyy.socialapp.entities.User;
import com.faddboyy.socialapp.repositories.UserRepository;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(username);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email " + username);
        }

        User user = userOptional.get();

        // 4. [PERUBAHAN PENTING]
        // Kita harus mengisi 'authorities'
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Logika untuk mengisi 'authorities'.
        // Jika Anda menyimpan role di database (misal: user.getRole()),
        // Anda bisa menambahkannya di sini:
        //
        // if (user.getRole() != null) {
        //     authorities.add(new SimpleGrantedAuthority(user.getRole()));
        // }
        
        // Untuk saat ini, kita beri semua user role "ROLE_USER"
        // Ini akan mengizinkan mereka mengakses endpoint .authenticated()
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities); // 5. 'authorities' sekarang sudah terisi
    }

}
