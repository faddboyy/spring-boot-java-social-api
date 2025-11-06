package com.faddboyy.socialapp.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "messages")
public class Message {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private String content;

    private String image; // Dibuat opsional (nullable = true)

    @ManyToOne
    private User user; // Pengirim pesan

    @ManyToOne
    private Chat chat; // Room chat tempat pesan ini berada

    private LocalDateTime timestamp;

    // --- Constructor ---

    /**
     * Constructor kosong (no-argument)
     * Wajib untuk JPA
     */
    public Message() {
        this.timestamp = LocalDateTime.now(); // Default timestamp
    }

    // --- Getters and Setters (Manual) ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}