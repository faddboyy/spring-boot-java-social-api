package com.faddboyy.socialapp.dto;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) untuk Comment.
 * Versi tanpa Lombok — semua getter, setter, dan constructor dibuat manual.
 */
public class CommentDto {

    private Integer id;
    private String content;
    private LocalDateTime createdAt;

    // Informasi user yang membuat comment (menggunakan DTO, bukan Entity)
    private UserDto user;

    // Hanya jumlah 'like' pada comment, bukan daftar lengkap user
    private Integer likesCount;

    // =============================
    // Constructors
    // =============================

    public CommentDto() {
    }

    public CommentDto(Integer id, String content, LocalDateTime createdAt, UserDto user, Integer likesCount) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.user = user;
        this.likesCount = likesCount;
    }

    // =============================
    // Getters and Setters
    // =============================

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    // =============================
    // Optional: toString()
    // =============================

    @Override
    public String toString() {
        return "CommentDto{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", user=" + (user != null ? user.getId() : null) + // Diasumsikan UserDto punya getId()
                ", likesCount=" + likesCount +
                '}';
    }
}