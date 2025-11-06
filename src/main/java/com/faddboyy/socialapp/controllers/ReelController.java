package com.faddboyy.socialapp.controllers;

import com.faddboyy.socialapp.request.CreateReelRequestDto;
import com.faddboyy.socialapp.dto.ReelsDto;
import com.faddboyy.socialapp.services.ReelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page; // [REFACTOR] Import
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal; // [REFACTOR] Import Principal
import java.util.List;
import java.util.Map; // Untuk response delete

@RestController
@RequestMapping("/api/reels")
public class ReelController {

    @Autowired
    private ReelService reelService;

    /**
     * [REFACTOR] Endpoint untuk membuat Reel baru (Aman).
     * User diambil dari Principal (JWT), bukan PathVariable.
     */
    @PostMapping("/") 
    public ResponseEntity<ReelsDto> createReel(
            @RequestBody CreateReelRequestDto requestDto,
            Principal principal // Mengambil user dari JWT
    ) {
        // principal.getName() akan berisi 'email'
        ReelsDto createdReel = reelService.createReel(requestDto, principal.getName());
        return new ResponseEntity<>(createdReel, HttpStatus.CREATED);
    }

    /**
     * [REFACTOR] Endpoint untuk mengambil semua reels (Menggunakan Paginasi).
     */
    @GetMapping("/")
    public ResponseEntity<Page<ReelsDto>> findAllReelsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size // Ambil 5 reels per halaman
    ) {
        Page<ReelsDto> allReels = reelService.findAllReelsPaginated(page, size);
        return new ResponseEntity<>(allReels, HttpStatus.OK);
    }

    /**
     * (Tetap sama) Endpoint untuk mengambil semua reels milik user tertentu.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReelsDto>> findReelsByUserId(@PathVariable Integer userId) {
        List<ReelsDto> userReels = reelService.findReelsByUserId(userId);
        return new ResponseEntity<>(userReels, HttpStatus.OK);
    }

    /**
     * [ENDPOINT BARU] Endpoint untuk menghapus reel.
     * Hanya user yang membuatnya yang bisa menghapus.
     */
    @DeleteMapping("/{reelId}")
    public ResponseEntity<?> deleteReel(
            @PathVariable Integer reelId,
            Principal principal // Untuk verifikasi pemilik
    ) {
        String message = reelService.deleteReel(reelId, principal.getName());
        
        // Mengembalikan response JSON yang rapi
        return new ResponseEntity<>(Map.of("message", message), HttpStatus.OK);
    }
}