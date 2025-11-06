package com.faddboyy.socialapp.services.implement;

import com.faddboyy.socialapp.request.CreateReelRequestDto;
import com.faddboyy.socialapp.dto.ReelsDto;
import com.faddboyy.socialapp.dto.UserDto;
import com.faddboyy.socialapp.entities.Reels;
import com.faddboyy.socialapp.entities.User;
import com.faddboyy.socialapp.repositories.ReelRepository;
// [DIHAPUS] import com.faddboyy.socialapp.repositories.UserRepository;
import com.faddboyy.socialapp.services.ReelService;
import com.faddboyy.socialapp.services.UserService; // [REFACTOR] Menggunakan UserService
import com.faddboyy.socialapp.exceptions.ForbiddenException; // [BARU] Import exception
import com.faddboyy.socialapp.exceptions.UrlNotFoundException; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page; // [REFACTOR] Import
import org.springframework.data.domain.PageRequest; // [REFACTOR] Import
import org.springframework.data.domain.Pageable; // [REFACTOR] Import
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReelServiceImplement implements ReelService {

    @Autowired
    private ReelRepository reelRepository;

    // [REFACTOR] Mengganti UserRepository dengan UserService agar konsisten
    @Autowired
    private UserService userService;

    @Override
    public ReelsDto createReel(CreateReelRequestDto requestDto, String userEmail) {
        
        // [REFACTOR] Menggunakan "workaround" UserService yang sudah kita sepakati
        UserDto userDto = userService.findUserByEmail(userEmail);
        User user = userService.findUserEntityById(userDto.getId());

        // 2. Buat entitas Reels baru
        Reels newReel = new Reels();
        newReel.setTitle(requestDto.getTitle());
        newReel.setVideo(requestDto.getVideo());
        newReel.setUser(user);

        // 3. Simpan ke database
        Reels savedReel = reelRepository.save(newReel);

        // 4. Konversi ke DTO dan kembalikan
        return convertToReelsDto(savedReel);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReelsDto> findAllReelsPaginated(int page, int size) {
        
        // [REFACTOR] Menerapkan Paginasi (sesuai diskusi kita sebelumnya)
        Pageable pageable = PageRequest.of(page, size);
        Page<Reels> reelPageEntity = reelRepository.findAll(pageable);

        // Menggunakan helper 'map' bawaan dari Page untuk konversi
        return reelPageEntity.map(this::convertToReelsDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReelsDto> findReelsByUserId(Integer userId) {
        
        // [REFACTOR] Menggunakan UserService untuk validasi (lebih konsisten)
        // Ini otomatis melempar 'UserNotFoundException' jika user tidak ada
        userService.findUserEntityById(userId); 

        // 2. Cari reels berdasarkan userId
        return reelRepository.findByUserId(userId)
                .stream()
                .map(this::convertToReelsDto)
                .collect(Collectors.toList());
    }

    // [METHOD BARU]
    @Override
    public String deleteReel(Integer reelId, String userEmail) {
        
        // 1. Ambil Reel yang ingin dihapus
        Reels reel = reelRepository.findById(reelId)
            .orElseThrow(() -> new UrlNotFoundException("Reel not found with id: " + reelId));

        // 2. Ambil user yang sedang login (meminta delete)
        UserDto userDto = userService.findUserByEmail(userEmail);
        User requestingUser = userService.findUserEntityById(userDto.getId());

        // 3. [LOGIKA KEAMANAN]
        // Cek apakah ID user yang meminta sama dengan ID user yang ada di reel
        if (!reel.getUser().getId().equals(requestingUser.getId())) {
            throw new ForbiddenException("You are not authorized to delete this reel.");
        }

        // 4. Jika aman, hapus reel
        reelRepository.delete(reel);

        return "Reel deleted successfully with id: " + reelId;
    }

    // --- HELPER METHODS (Mapper) ---

    private ReelsDto convertToReelsDto(Reels reel) {
        if (reel == null) return null; 

        ReelsDto dto = new ReelsDto();
        dto.setId(reel.getId());
        dto.setTitle(reel.getTitle());
        dto.setVideo(reel.getVideo());

        // [REFACTOR] Menggunakan helper UserService agar konsisten
        if (reel.getUser() != null) {
            dto.setUser(userService.convertUserToDto(reel.getUser()));
        }

        return dto;
    }
}