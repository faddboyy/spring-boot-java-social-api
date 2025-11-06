package com.faddboyy.socialapp.dto;

import java.time.LocalDateTime;

import com.faddboyy.socialapp.dto.UserDto;

public class PostDto {
    
    private Integer id;
    private String caption;
    private String image;
    private String video;
    private LocalDateTime createdAt;
    
    private UserDto user; 
    
    private int likesCount;
    
    // =============================
    // [FIELD BARU DITAMBAHKAN]
    // =============================
    private int saveCount;

    // Constructor kosong untuk Jackson
    public PostDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
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

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    // =============================
    // [GETTER/SETTER BARU DITAMBAHKAN]
    // =============================
    public int getSaveCount() {
        return saveCount;
    }

    public void setSaveCount(int saveCount) {
        this.saveCount = saveCount;
    }
}