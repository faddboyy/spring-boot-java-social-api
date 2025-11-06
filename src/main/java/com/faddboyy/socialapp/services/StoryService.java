package com.faddboyy.socialapp.services;

import com.faddboyy.socialapp.request.CreateStoryRequestDto;
import com.faddboyy.socialapp.dto.StoryDto;
import com.faddboyy.socialapp.dto.UserDto; // Import UserDto

import java.util.List;

public interface StoryService {
    
    /**
     * [REFACTOR] Menggunakan email dari Principal, bukan userId.
     */
    StoryDto createStory(CreateStoryRequestDto request, String userEmail);

    /**
     * Menemukan semua story AKTIF (24 jam) milik seorang user.
     */
    List<StoryDto> findStoryByUserId(Integer userId);

    /**
     * [BARU] Mendapatkan daftar user (mutuals) yang memiliki story aktif
     * untuk ditampilkan di feed (lingkaran-lingkaran story).
     */
    List<UserDto> getStoryFeed(String userEmail);

    /**
     * [BARU] Menghapus story.
     */
    String deleteStory(Integer storyId, String userEmail);
}