package com.faddboyy.socialapp.services.implement;

import com.faddboyy.socialapp.dto.MessageDto;
import com.faddboyy.socialapp.dto.UserDto;
import com.faddboyy.socialapp.entities.Chat;
import com.faddboyy.socialapp.entities.Message;
import com.faddboyy.socialapp.entities.User;
// [GANTI KE EXCEPTION ANDA]
import com.faddboyy.socialapp.exceptions.ForbiddenException;
import com.faddboyy.socialapp.exceptions.UrlNotFoundException;
// ...
import com.faddboyy.socialapp.repositories.MessageRepository;
import com.faddboyy.socialapp.request.CreateMessageRequestDto;
import com.faddboyy.socialapp.services.ChatService;
import com.faddboyy.socialapp.services.MessageService;
import com.faddboyy.socialapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MessageServiceImplement implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserService userService; // Sesuai arsitektur

    @Autowired
    private ChatService chatService; // Sesuai arsitektur

    @Override
    public MessageDto createMessage(CreateMessageRequestDto request, Integer chatId, String senderEmail) {

        // [PERBAIKAN KUNCI 1]
        // Menerapkan 'workaround' untuk mendapatkan Entity User dari Email
        // 1. Dapatkan DTO (ini method public yang ada di UserService)
        UserDto senderDto = userService.findUserByEmail(senderEmail);
        // 2. Dapatkan Entity dari ID (ini method public yang ada di UserService)
        User senderUser = userService.findUserEntityById(senderDto.getId());

        // 2. Dapatkan Entity Chat (room) dari ChatService (PRASYARAT)
        // Pastikan ChatService memiliki helper public 'findChatEntityById'
        Chat chatRoom = chatService.findChatEntityById(chatId);

        // 3. [Keamanan] Pastikan user adalah bagian dari chat
        if (!chatRoom.getUsers().contains(senderUser)) {
            // Sebaiknya buat exception kustom untuk ini
            throw new ForbiddenException("You are not a member of this chat.");
        }

        // 4. Buat Entity Message baru
        Message newMessage = new Message();
        newMessage.setChat(chatRoom);
        newMessage.setUser(senderUser);
        newMessage.setContent(request.getContent());
        newMessage.setImage(request.getImage());
        newMessage.setTimestamp(LocalDateTime.now());

        // 5. Simpan ke database
        Message savedMessage = messageRepository.save(newMessage);

        // 6. Konversi ke DTO dan kembalikan
        return convertToMessageDto(savedMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageDto> findMessagesByChatId(Integer chatId, String userEmail) {

        // [PERBAIKAN KUNCI 2]
        // Menerapkan 'workaround' untuk validasi keamanan
        // 1. Dapatkan DTO (dari email)
        UserDto requestingUserDto = userService.findUserByEmail(userEmail);
        // 2. Dapatkan Entity (dari ID)
        User requestingUser = userService.findUserEntityById(requestingUserDto.getId());

        // 2. Dapatkan Entity Chat (room) dari ChatService
        Chat chatRoom = chatService.findChatEntityById(chatId);

        // 3. [Keamanan] Pastikan user adalah bagian dari chat
        if (!chatRoom.getUsers().contains(requestingUser)) {
            throw new ForbiddenException("You are not authorized to view this chat.");
        }

        // 4. Jika aman, ambil semua pesan dari repository
        List<Message> messages = messageRepository.findByChatId(chatId);

        // 5. Konversi ke List DTO dan kembalikan
        return messages.stream()
                .map(this::convertToMessageDto)
                .collect(Collectors.toList());
    }

    // --- HELPER MAPPER ---

    /**
     * Konversi Entity Message -> MessageDto
     * Menggunakan helper dari UserService
     */
    private MessageDto convertToMessageDto(Message message) {
        if (message == null)
            return null;

        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setContent(message.getContent());
        dto.setImage(message.getImage());
        dto.setTimestamp(message.getTimestamp());

        // [SESUAI] Mendelegasikan konversi User ke UserService
        if (message.getUser() != null) {
            dto.setUser(userService.convertUserToDto(message.getUser()));
        }

        if (message.getChat() != null) {
            dto.setChatId(message.getChat().getId());
        }

        return dto;
    }

    @Override
    public String deleteMessage(Integer messageId, String userEmail) {
        
        // 1. Ambil Message entity dari database
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new UrlNotFoundException("Message not found with id: " + messageId));

        // 2. Ambil User entity (yang meminta delete)
        //    (Menggunakan workaround yang sudah ada)
        UserDto requestingUserDto = userService.findUserByEmail(userEmail);
        User requestingUser = userService.findUserEntityById(requestingUserDto.getId());

        // 3. [LOGIKA KEAMANAN]
        //    Cek apakah user yang request SAMA DENGAN user yang mengirim pesan
        if (!message.getUser().getId().equals(requestingUser.getId())) {
            // Jika tidak sama, lempar exception
            throw new ForbiddenException("You are not authorized to delete this message.");
        }

        // 4. Jika aman (lolos pengecekan), hapus pesan
        messageRepository.delete(message);

        return "Message deleted successfully";
    }
}