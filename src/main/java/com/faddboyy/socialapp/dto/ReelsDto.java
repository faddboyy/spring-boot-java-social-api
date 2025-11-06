package com.faddboyy.socialapp.dto;

public class ReelsDto {
    private Long id;
    private String title;
    private String video;
    private UserDto user; // Menggunakan UserDto, bukan Entitas User

    // Constructors
    public ReelsDto() {
    }

    public ReelsDto(Long id, String title, String video, UserDto user) {
        this.id = id;
        this.title = title;
        this.video = video;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}