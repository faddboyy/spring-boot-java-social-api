package com.faddboyy.socialapp.entities;

import jakarta.persistence.*;

@Entity
@Table(name="reels")
public class Reels {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String video;

    @ManyToOne
    private User user;

    public Reels() {
    }

    public Reels(String title, String video, User user) {
        this.title = title;
        this.video = video;
        this.user = user;
    }

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
