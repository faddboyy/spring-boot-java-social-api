package com.faddboyy.socialapp.entities;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "stories") // Menambahkan @Table untuk konsistensi
public class Story {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private User user;

    private String image;

    private String captions;

    private LocalDateTime timestamp;

    // --- Constructor ---

    /**
     * Constructor kosong (no-argument).
     * Wajib ada untuk JPA.
     */
    public Story() {
    }

    /**
     * Constructor kustom untuk membuat story baru.
     * Timestamp di-generate secara otomatis.
     */
    public Story(User user, String image, String captions) {
        this.user = user;
        this.image = image;
        this.captions = captions;
        this.timestamp = LocalDateTime.now(); // Otomatis set waktu saat ini
    }

    // --- Getters and Setters (Manual) ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCaptions() {
        return captions;
    }

    public void setCaptions(String captions) {
        this.captions = captions;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}