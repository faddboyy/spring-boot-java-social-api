package com.faddboyy.socialapp.dto;

import com.faddboyy.socialapp.entities.User;

public class UserDto {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    
    private Integer followerCount;
    private Integer followingCount;
    private Integer postCount;

    // =============================
    // [CONSTRUCTOR BARU DITAMBAHKAN]
    // =============================

    /**
     * Constructor kosong (no-argument).
     * Dibutuhkan oleh framework seperti Jackson.
     */
    public UserDto() {
        // Kosong
    }

    /**
     * Constructor Konversi.
     * Mengubah Entity 'User' menjadi 'UserDto'.
     * Ini adalah bagian yang dipanggil oleh service kita.
     */
    public UserDto(User user) {
        if (user == null) {
            return;
        }
        
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.gender = user.getGender();

        // Mengisi count berdasarkan ukuran list dari entity
        // Pastikan entity 'User' Anda memiliki method getFollowers(), getFollowing(), dan getPosts()
        // yang mengembalikan Collection (seperti List atau Set).
        
        // Asumsi: user.getFollowers() mengembalikan List<User>
        this.followerCount = (user.getFollowers() != null) ? user.getFollowers().size() : 0;
        
        // Asumsi: user.getFollowing() mengembalikan List<User>
        this.followingCount = (user.getFollowings() != null) ? user.getFollowings().size() : 0;
        
        // Asumsi: user.getPosts() mengembalikan List<Post>
        this.postCount = (user.getPosts() != null) ? user.getPosts().size() : 0;
    }


    // =============================
    // (Getter & Setter Anda - tidak perlu diubah)
    // =============================

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public Integer getFollowerCount() {
        return followerCount;
    }
    public void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }
    public Integer getFollowingCount() {
        return followingCount;
    }
    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }
    public Integer getPostCount() {
        return postCount;
    }
    public void setPostCount(Integer postCount) {
        this.postCount = postCount;
    }
}