package com.faddboyy.socialapp.repositories;

import com.faddboyy.socialapp.entities.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StoryRepository extends JpaRepository<Story, Integer> {
    
    /**
     * [BARU] Menemukan semua story milik user TAPI hanya yang masih aktif
     * (dibuat SETELAH waktu kedaluwarsa).
     */
    List<Story> findByUserIdAndTimestampAfter(Integer userId, LocalDateTime timestamp);

    /**
     * [BARU] Cek apakah seorang user punya story yang masih aktif.
     * Jauh lebih cepat daripada mengambil List-nya.
     */
    boolean existsByUserIdAndTimestampAfter(Integer userId, LocalDateTime timestamp);

    /**
     * [BARU] Menghapus semua story yang dibuat SEBELUM waktu kedaluwarsa.
     * Digunakan oleh @Scheduled task.
     */
    void deleteByTimestampBefore(LocalDateTime timestamp);
}