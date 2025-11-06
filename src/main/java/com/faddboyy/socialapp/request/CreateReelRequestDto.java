package com.faddboyy.socialapp.request;

// Anda bisa juga menggunakan validasi di sini, misal @NotEmpty
public class CreateReelRequestDto {
    private String title;
    private String video;

    // Constructors
    public CreateReelRequestDto() {
    }

    public CreateReelRequestDto(String title, String video) {
        this.title = title;
        this.video = video;
    }

    // Getters and Setters
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
}