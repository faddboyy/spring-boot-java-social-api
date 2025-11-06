package com.faddboyy.socialapp.services.implement;

import com.faddboyy.socialapp.dto.StoryDto;
import com.faddboyy.socialapp.dto.UserDto;
import com.faddboyy.socialapp.entities.Story;
import com.faddboyy.socialapp.entities.User;
import com.faddboyy.socialapp.exceptions.ForbiddenException;
import com.faddboyy.socialapp.exceptions.UrlNotFoundException;
import com.faddboyy.socialapp.repositories.StoryRepository;
import com.faddboyy.socialapp.request.CreateStoryRequestDto;
import com.faddboyy.socialapp.services.StoryService;
import com.faddboyy.socialapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled; // Import untuk auto-delete
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;

@Service
@Transactional
public class StoryServiceImplement implements StoryService {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private UserService userService; // Konsisten dengan arsitektur lain

    // Batas waktu story (24 jam)
    private final long STORY_DURATION_HOURS = 24;

    @Override
    public StoryDto createStory(CreateStoryRequestDto request, String userEmail) {

        // [REFACTOR] Menggunakan "workaround" UserService
        UserDto userDto = userService.findUserByEmail(userEmail);
        User user = userService.findUserEntityById(userDto.getId());

        Story newStory = new Story();
        newStory.setUser(user);
        newStory.setImage(request.getImage());
        newStory.setCaptions(request.getCaption());
        newStory.setTimestamp(LocalDateTime.now()); // Set waktu pembuatan

        Story savedStory = storyRepository.save(newStory);
        return convertToStoryDto(savedStory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoryDto> findStoryByUserId(Integer userId) {

        // Validasi user ada
        User user = userService.findUserEntityById(userId);

        // [LOGIKA 24 JAM] Tentukan batas waktu kedaluwarsa
        LocalDateTime expirationTime = LocalDateTime.now().minusHours(STORY_DURATION_HOURS);

        // Ambil story user tsb yang dibuat SETELAH waktu kedaluwarsa
        List<Story> stories = storyRepository.findByUserIdAndTimestampAfter(user.getId(), expirationTime);

        return stories.stream()
                .map(this::convertToStoryDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getStoryFeed(String userEmail) {

        User requestingUser = userService.findUserEntityById(
                userService.findUserByEmail(userEmail).getId());

        // [PERBAIKAN DI SINI]
        // 1. Ambil daftar following dan followers
        // Tipe data diubah dari 'List' menjadi 'Set' agar sesuai dengan User.java
        Set<User> followings = requestingUser.getFollowings();
        Set<User> followers = requestingUser.getFollowers();

        // 2. [LOGIKA MUTUALS] Cari irisan (intersection)
        // Logika stream() dan filter() di bawah ini sudah BISA
        // bekerja dengan 'Set' tanpa perlu diubah.
        List<User> mutuals = followings.stream()
                // followers::contains sangat cepat di 'Set' (HashSet O(1))
                .filter(followers::contains)
                .collect(Collectors.toList());

        // 3. Tentukan batas waktu kedaluwarsa
        LocalDateTime expirationTime = LocalDateTime.now().minusHours(STORY_DURATION_HOURS);

        // 4. Filter daftar mutuals: HANYA yang punya story aktif
        return mutuals.stream()
                .filter(user -> storyRepository.existsByUserIdAndTimestampAfter(user.getId(), expirationTime))
                .map(user -> userService.convertUserToDto(user)) // Konversi ke UserDto
                .collect(Collectors.toList());
    }

    @Override
    public String deleteStory(Integer storyId, String userEmail) {

        // 1. Ambil Story
        Story story = storyRepository.findById(storyId)
                .orElseThrow(() -> new UrlNotFoundException("Story not found with id: " + storyId));

        // 2. Ambil User (yang meminta)
        User requestingUser = userService.findUserEntityById(
                userService.findUserByEmail(userEmail).getId());

        // 3. [KEAMANAN] Verifikasi kepemilikan
        if (!story.getUser().getId().equals(requestingUser.getId())) {
            throw new ForbiddenException("You are not authorized to delete this story.");
        }

        // 4. Hapus
        storyRepository.delete(story);
        return "Story deleted successfully with id: " + storyId;
    }

    // --- HELPER MAPPER ---
    private StoryDto convertToStoryDto(Story story) {
        if (story == null)
            return null;
        StoryDto dto = new StoryDto();
        dto.setId(story.getId());
        dto.setImage(story.getImage());
        dto.setCaption(story.getCaptions());
        dto.setTimestamp(story.getTimestamp());

        // Konsisten: mendelegasikan konversi User ke UserService
        if (story.getUser() != null) {
            dto.setUser(userService.convertUserToDto(story.getUser()));
        }
        return dto;
    }

    // --- [LOGIKA AUTO-DELETE 24 JAM] ---
    /**
     * Method ini akan berjalan otomatis setiap 1 jam (3600000 ms).
     * Ia akan menghapus semua story yang lebih tua dari 24 jam.
     */
    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void deleteExpiredStories() {
        LocalDateTime expirationTime = LocalDateTime.now().minusHours(STORY_DURATION_HOURS);
        storyRepository.deleteByTimestampBefore(expirationTime);
        System.out.println("Running scheduled job: Deleting expired stories...");
    }
}