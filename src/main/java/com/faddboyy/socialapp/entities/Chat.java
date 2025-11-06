package com.faddboyy.socialapp.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "chats") // Menambahkan @Table
public class Chat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String contact_name;

    private String contact_image;

    @ManyToMany
    private List<User> users = new ArrayList<>(); // Inisialisasi list itu bagus

    private LocalDateTime timestamp;

    // --- Constructor ---

    /**
     * Constructor kosong (no-argument).
     * Wajib ada untuk JPA.
     */
    public Chat() {
        this.timestamp = LocalDateTime.now(); // Set timestamp saat dibuat
    }

    // --- Getters and Setters (Manual) ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}