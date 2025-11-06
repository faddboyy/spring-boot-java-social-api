package com.faddboyy.socialapp.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String caption;

    @Column(nullable = true)
    private String image;

    @Column(nullable = true)
    private String video;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "post_likes",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> liked = new HashSet<>();

    @ManyToMany(mappedBy = "savedPost", fetch = FetchType.LAZY)
    private Set<User> savedByUsers = new HashSet<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    public Post() {}

    public Post(String caption, String image, String video, User user) {
        this.caption = caption;
        this.image = image;
        this.video = video;
        this.user = user;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getter & Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getVideo() { return video; }
    public void setVideo(String video) { this.video = video; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Set<User> getLiked() { return liked; }
    public void setLiked(Set<User> liked) { this.liked = liked; }

    public Set<User> getSavedByUsers() { return savedByUsers; }
    public void setSavedByUsers(Set<User> savedByUsers) { this.savedByUsers = savedByUsers; }

    public List<Comment> getComments() { return comments; }
    public void setComments(List<Comment> comments) { this.comments = comments; }

    @Override
    public String toString() {
        return "Post{id=" + id + ", caption='" + caption + "', createdAt=" + createdAt + "}";
    }
}
