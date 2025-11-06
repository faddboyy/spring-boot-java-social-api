package com.faddboyy.socialapp.controllers;

import com.faddboyy.socialapp.request.CreateStoryRequestDto;
import com.faddboyy.socialapp.dto.StoryDto;
import com.faddboyy.socialapp.dto.UserDto; // [BARU] Import UserDto
import com.faddboyy.socialapp.services.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal; // [BARU] Import Principal
import java.util.List;
import java.util.Map; // [BARU] Import Map

@RestController
@RequestMapping("/api/stories")
public class StoryController {

    @Autowired
    private StoryService storyService;

    /**
     * [REFACTOR] Endpoint untuk membuat Story baru (Aman).
     * User diambil dari Principal (JWT).
     */
    @PostMapping("/")
    public ResponseEntity<StoryDto> createStory(
            @RequestBody CreateStoryRequestDto storyDto,
            Principal principal // Mengambil user dari JWT
    ) {
        // principal.getName() akan berisi 'email'
        StoryDto createdStory = storyService.createStory(storyDto, principal.getName());
        return new ResponseEntity<>(createdStory, HttpStatus.CREATED);
    }

    /**
     * (Tetap sama) Endpoint untuk mengambil semua story AKTIF
     * milik seorang user (saat user mengklik lingkaran story).
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<StoryDto>> findStoryByUserId(
            @PathVariable Integer userId
    ) {
        List<StoryDto> stories = storyService.findStoryByUserId(userId);
        return new ResponseEntity<>(stories, HttpStatus.OK);
    }

    /**
     * [BARU] Endpoint untuk 'Story Feed'.
     * Mengambil daftar teman MUTUAL yang memiliki story aktif.
     */
    @GetMapping("/feed")
    public ResponseEntity<List<UserDto>> getStoryFeed(
            Principal principal
    ) {
        List<UserDto> userFeed = storyService.getStoryFeed(principal.getName());
        return new ResponseEntity<>(userFeed, HttpStatus.OK);
    }

    /**
     * [BARU] Endpoint untuk menghapus story.
     * Hanya pemilik yang bisa menghapus.
     */
    @DeleteMapping("/{storyId}")
    public ResponseEntity<?> deleteStory(
            @PathVariable Integer storyId,
            Principal principal // Untuk verifikasi
    ) {
        String message = storyService.deleteStory(storyId, principal.getName());
        return new ResponseEntity<>(Map.of("message", message), HttpStatus.OK);
    }
}