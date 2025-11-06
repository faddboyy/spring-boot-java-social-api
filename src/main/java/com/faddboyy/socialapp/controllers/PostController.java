package com.faddboyy.socialapp.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.faddboyy.socialapp.dto.PostDto;
import com.faddboyy.socialapp.dto.UserDto;
import com.faddboyy.socialapp.entities.Post;
import com.faddboyy.socialapp.response.ApiResponse;
import com.faddboyy.socialapp.services.PostService;
import com.faddboyy.socialapp.services.UserService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    private UserDto getAuthenticatedUser(Authentication auth) {
        String email = auth.getName();
        return userService.findUserByEmail(email);
    }

    @PostMapping("/")
    public ResponseEntity<PostDto> createPost(@RequestBody Post post, Authentication auth) {
        UserDto authUser = getAuthenticatedUser(auth);
        PostDto createdPostDto = postService.createNewPost(post, authUser.getId());
        return new ResponseEntity<>(createdPostDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse> deletePost(
            @PathVariable Integer postId, 
            Authentication auth) {
        
        UserDto authUser = getAuthenticatedUser(auth);
        String message = postService.deletePost(postId, authUser.getId());
        ApiResponse res = new ApiResponse(message, true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> findPostByIdHandler(@PathVariable Integer postId) {
        PostDto postDto = postService.findPostById(postId);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<PostDto>> findPostByUserId(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<PostDto> postsDto = postService.findPostByUserIdPaginated(userId, page, size);
        return new ResponseEntity<>(postsDto, HttpStatus.OK);
    }
    
    @GetMapping("/user/me")
    public ResponseEntity<Page<PostDto>> findMyPosts(
            Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UserDto authUser = getAuthenticatedUser(auth);
        Page<PostDto> postsDto = postService.findPostByUserIdPaginated(authUser.getId(), page, size);
        return new ResponseEntity<>(postsDto, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<Page<PostDto>> findPostsPaginated(
            @RequestParam(value = "search", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<PostDto> postsDto = postService.findPostsPaginated(keyword, page, size);
        return new ResponseEntity<>(postsDto, HttpStatus.OK);
    }

    @PutMapping("/save/{postId}")
    public ResponseEntity<PostDto> savedPostHandler(
            @PathVariable Integer postId, 
            Authentication auth) {
        
        UserDto authUser = getAuthenticatedUser(auth);
        PostDto postDto = postService.savedPost(postId, authUser.getId());
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    @PutMapping("/like/{postId}")
    public ResponseEntity<PostDto> likedPostHandler(
            @PathVariable Integer postId, 
            Authentication auth) {
        
        UserDto authUser = getAuthenticatedUser(auth);
        PostDto postDto = postService.likePost(postId, authUser.getId());
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }
}