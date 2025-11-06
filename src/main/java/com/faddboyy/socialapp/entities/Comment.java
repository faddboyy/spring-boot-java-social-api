package com.faddboyy.socialapp.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// [DIHAPUS] import com.faddboyy.socialapp.dto.UserDto; // Entity tidak boleh bergantung pada DTO

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500)
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "comment_likes",
        joinColumns = @JoinColumn(name = "comment_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> liked = new ArrayList<>();

    public Comment() {}

    public Comment(String content, User user, Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Getter & Setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }

    // [PERBAIKAN] Tipe parameter diubah dari UserDto ke User
    public void setUser(User user) { 
        this.user = user; 
    }

    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }

    public List<User> getLiked() { return liked; }
    public void setLiked(List<User> liked) { this.liked = liked; }

    @Override
    public String toString() {
        return "Comment{id=" + id + ", content='" + content + "', createdAt=" + createdAt + "}";
    }
}