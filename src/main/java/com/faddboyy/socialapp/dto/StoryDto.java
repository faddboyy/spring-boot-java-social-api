package com.faddboyy.socialapp.dto;

import com.faddboyy.socialapp.dto.UserDto;
import java.time.LocalDateTime;

public class StoryDto {
    
    private Integer id;
    private String image;
    private String caption;
    private UserDto user; // User yang mem-posting
    private LocalDateTime timestamp;

    // Getters and Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getCaption() {
        return caption;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }
    public UserDto getUser() {
        return user;
    }
    public void setUser(UserDto user) {
        this.user = user;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}