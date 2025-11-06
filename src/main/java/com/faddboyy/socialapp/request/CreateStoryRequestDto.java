package com.faddboyy.socialapp.request;

// DTO ini adalah apa yang dikirim frontend (JSON)
public class CreateStoryRequestDto {
    
    private String image;
    private String caption;

    // Getters and Setters
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
}