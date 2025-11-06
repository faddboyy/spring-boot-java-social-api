package com.faddboyy.socialapp.request;

// DTO ini berisi apa yang dikirim frontend saat membuat pesan baru
public class CreateMessageRequestDto {
    
    private String content;
    private String image;

    // Getters and Setters
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
}