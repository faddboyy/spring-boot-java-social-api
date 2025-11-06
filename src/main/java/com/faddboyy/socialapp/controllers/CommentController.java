package com.faddboyy.socialapp.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; 
import org.springframework.web.bind.annotation.*;

import com.faddboyy.socialapp.dto.CommentDto;
import com.faddboyy.socialapp.dto.UserDto;
import com.faddboyy.socialapp.entities.Comment;
import com.faddboyy.socialapp.services.CommentService;
import com.faddboyy.socialapp.services.UserService; 

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final UserService userService; 

    public CommentController(CommentService commentService, UserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping("/post/{postId}")
    public ResponseEntity<CommentDto> createComment(
            @RequestBody Comment comment, 
            @PathVariable Integer postId,
            Authentication auth) throws Exception { // Tambahkan throws Exception
        
        UserDto authUser = userService.findUserByEmail(auth.getName());

        // Ini sekarang akan bekerja karena createComment MENGEMBALIKAN CommentDto
        CommentDto createdComment = commentService.createComment(comment, postId, authUser.getId());
        
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @PutMapping("/like/{commentId}")
    public ResponseEntity<CommentDto> likeComment(
            @PathVariable Integer commentId,
            Authentication auth) throws Exception { // Tambahkan throws Exception
        
        UserDto authUser = userService.findUserByEmail(auth.getName());

        // Ini sekarang akan bekerja karena likeComment MENGEMBALIKAN CommentDto
        CommentDto likedComment = commentService.likeComment(commentId, authUser.getId());
        
        return new ResponseEntity<>(likedComment, HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Integer commentId) throws Exception { // Tambahkan throws Exception
        // Ini sekarang akan bekerja karena findCommentById MENGEMBALIKAN CommentDto
        CommentDto commentDto = commentService.findCommentById(commentId);
        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Integer commentId,
            Authentication auth) throws Exception { // Menjaga konsistensi 'throws Exception'
        
        // 1. Dapatkan DTO user yang diautentikasi
        UserDto authUser = userService.findUserByEmail(auth.getName());

        // 2. Panggil service dengan commentId dan userId
        String message = commentService.deleteComment(commentId, authUser.getId());

        // 3. Kembalikan pesan sukses dalam format JSON
        return new ResponseEntity<>(Map.of("message", message), HttpStatus.OK);
    }
}