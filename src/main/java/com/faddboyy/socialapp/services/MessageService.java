package com.faddboyy.socialapp.services;

import com.faddboyy.socialapp.dto.MessageDto;
import com.faddboyy.socialapp.request.CreateMessageRequestDto;
import java.util.List;

// Mengubah 'class' di template Anda menjadi 'interface'
public interface MessageService {
    
    /**
     * Membuat pesan baru di dalam chat room.
     * @param request DTO berisi content dan image
     * @param chatId ID dari Chat room
     * @param senderEmail Email dari pengirim (diambil dari Principal)
     * @return DTO dari pesan yang baru dibuat
     */
    MessageDto createMessage(CreateMessageRequestDto request, Integer chatId, String senderEmail);

    /**
     * Menemukan semua riwayat pesan dari satu chat room.
     * @param chatId ID dari Chat room
     * @param userEmail Email dari user yang meminta (untuk validasi keamanan)
     * @return List dari MessageDto
     */
    List<MessageDto> findMessagesByChatId(Integer chatId, String userEmail);

    /**
     * Menghapus pesan untuk semua orang.
     * Hanya bisa dilakukan oleh user yang mengirim pesan tsb.
     * @param messageId ID pesan yang akan dihapus
     * @param userEmail Email user yang meminta delete (dari Principal)
     * @return Pesan sukses
     */
    String deleteMessage(Integer messageId, String userEmail);
}