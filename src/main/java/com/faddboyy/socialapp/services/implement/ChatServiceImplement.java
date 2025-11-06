package com.faddboyy.socialapp.services.implement;

import com.faddboyy.socialapp.dto.ChatDto;
import com.faddboyy.socialapp.request.CreateChatRequestDto;
import com.faddboyy.socialapp.dto.UserDto;
import com.faddboyy.socialapp.entities.Chat;
import com.faddboyy.socialapp.entities.User;
import com.faddboyy.socialapp.exceptions.UrlNotFoundException; // Gunakan exception Anda
import com.faddboyy.socialapp.repositories.ChatRepository;
import com.faddboyy.socialapp.services.ChatService;
import com.faddboyy.socialapp.services.UserService; // Import UserService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatServiceImplement implements ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserService userService; // Menggunakan UserService

    @Override
    public ChatDto createChat(CreateChatRequestDto request, String creatorEmail) {

        UserDto creatorDto = userService.findUserByEmail(creatorEmail);
        User creatorUser = userService.findUserEntityById(creatorDto.getId());

        // [LOGIKA TAMBAHAN DIMULAI DI SINI]

        // 1. Cek apakah ini permintaan untuk CHAT GRUP (banyak user ID atau ada nama
        // grup)
        boolean isGroupChat = (request.getUserIds() != null && request.getUserIds().size() > 1) ||
                (request.getContact_name() != null && !request.getContact_name().isEmpty());

        // 2. Jika ini BUKAN grup chat (ini adalah 1-on-1) dan HANYA ada 1 user ID
        if (!isGroupChat && request.getUserIds() != null && request.getUserIds().size() == 1) {

            Integer otherUserId = request.getUserIds().get(0);
            User otherUser = userService.findUserEntityById(otherUserId);

            // 3. Panggil Repository untuk mencari chat yang ada
            // (Anda perlu menambahkan method 'findChatByTwoUsers' ke ChatRepository)
            Optional<Chat> existingChat = chatRepository.findChatByTwoUsers(creatorUser.getId(), otherUser.getId());

            if (existingChat.isPresent()) {
                // JANGAN BUAT CHAT BARU. Langsung kembalikan chat yang sudah ada.
                return convertToChatDto(existingChat.get());
            }
        }

        // [LOGIKA TAMBAHAN SELESAI]

        // 4. Jika ini adalah Grup Chat, atau Chat 1-on-1 yang belum ada,
        // maka lanjutkan logika Anda yang sudah benar...

        Chat newChat = new Chat();
        newChat.getUsers().add(creatorUser); // Tambahkan pembuat

        if (request.getUserIds() != null) {
            for (Integer userId : request.getUserIds()) {
                User otherUser = userService.findUserEntityById(userId);
                newChat.getUsers().add(otherUser);
            }
        }

        newChat.setContact_name(request.getContact_name());
        newChat.setContact_image(request.getContact_image());

        Chat savedChat = chatRepository.save(newChat);

        return convertToChatDto(savedChat);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatDto> findChatsByEmail(String email) {

        // [REFACTOR KUNCI] "Workaround" untuk mendapatkan ID dari Email:
        // 1. Dapatkan DTO dari Email
        UserDto userDto = userService.findUserByEmail(email);

        // 2. Gunakan ID dari DTO untuk query
        List<Chat> chats = chatRepository.findByUsersId(userDto.getId());

        return chats.stream()
                .map(this::convertToChatDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ChatDto findChatById(Integer chatId) {
        Chat chat = findChatByIdInternal(chatId);
        return convertToChatDto(chat);
    }

    @Override
    public Chat findChatEntityById(Integer chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new UrlNotFoundException("Chat not found with id: " + chatId));
    }

    // --- HELPER METHODS ---

    private Chat findChatByIdInternal(Integer chatId) {
        // Ganti 'UrlNotFoundException' jika Anda punya 'ChatNotFoundException'
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new UrlNotFoundException("Chat not found with id: " + chatId));
    }

    /**
     * Helper konversi Chat -> ChatDto
     */
    private ChatDto convertToChatDto(Chat chat) {
        if (chat == null)
            return null;

        ChatDto dto = new ChatDto();
        dto.setId(chat.getId());
        dto.setContact_name(chat.getContact_name());
        dto.setContact_image(chat.getContact_image());
        dto.setTimestamp(chat.getTimestamp());

        if (chat.getUsers() != null) {
            dto.setUsers(
                    chat.getUsers().stream()
                            // [SESUAI] Menggunakan helper DTO dari UserService
                            .map(user -> userService.convertUserToDto(user))
                            .collect(Collectors.toList()));
        }

        return dto;
    }
}