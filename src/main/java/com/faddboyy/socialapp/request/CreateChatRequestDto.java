package com.faddboyy.socialapp.request;

import java.util.List;

public class CreateChatRequestDto {
    
    // ID dari user-user yang akan ditambahkan ke chat
    // (selain user yang membuat chat)
    private List<Integer> userIds;

    // Opsional: nama dan gambar grup chat
    private String contact_name;
    private String contact_image;

    // Getters and Setters
    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getContact_image() {
        return contact_image;
    }

    public void setContact_image(String contact_image) {
        this.contact_image = contact_image;
    }
}