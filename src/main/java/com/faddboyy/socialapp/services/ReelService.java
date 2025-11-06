package com.faddboyy.socialapp.services;

import com.faddboyy.socialapp.request.CreateReelRequestDto;
import com.faddboyy.socialapp.dto.ReelsDto;
import org.springframework.data.domain.Page; // Import Page

import java.util.List;

public interface ReelService {

    /**
     * [REFACTOR] Signature diubah. Menggunakan email dari Principal, bukan userId.
     */
    ReelsDto createReel(CreateReelRequestDto requestDto, String userEmail);

    /**
     * [REFACTOR] Diubah agar menggunakan Paginasi.
     */
    Page<ReelsDto> findAllReelsPaginated(int page, int size);

    /**
     * (Tetap sama) Menemukan semua reels milik user tertentu.
     */
    List<ReelsDto> findReelsByUserId(Integer userId);

    /**
     * [BARU] Menghapus reel.
     * @param reelId ID reel yang akan dihapus
     * @param userEmail Email user yang meminta delete (dari Principal)
     * @return Pesan sukses
     */
    String deleteReel(Integer reelId, String userEmail);
}