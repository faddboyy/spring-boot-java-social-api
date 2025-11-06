package com.faddboyy.socialapp.services.implement;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faddboyy.socialapp.exceptions.ForbiddenException;
// Import Exception kustom Anda
import com.faddboyy.socialapp.exceptions.UrlNotFoundException;
import com.faddboyy.socialapp.repositories.CommentRepository;
import com.faddboyy.socialapp.repositories.PostRepository;
import com.faddboyy.socialapp.services.CommentService;
import com.faddboyy.socialapp.services.PostService;
import com.faddboyy.socialapp.services.UserService;

// Import DTOs yang diperlukan untuk konversi
import com.faddboyy.socialapp.dto.CommentDto;
import com.faddboyy.socialapp.dto.UserDto;
import com.faddboyy.socialapp.entities.Comment;
import com.faddboyy.socialapp.entities.Post;
import com.faddboyy.socialapp.entities.User;

@Service
public class CommentServiceImplement implements CommentService {

    @Autowired
    private PostService postService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;

    @Override
    public CommentDto createComment(Comment comment, Integer postId, Integer userId) { // Hapus 'throws Exception'

        // Metode ini sudah aman karena 'findUserEntityById' dan 'findPostEntityById'
        // sudah melempar exception '...NotFound' mereka sendiri.
        User user = userService.findUserEntityById(userId);
        Post post = postService.findPostEntityById(postId);

        comment.setUser(user);
        comment.setPost(post);
        comment.setCreatedAt(LocalDateTime.now()); // Pastikan createdAt di-set jika perlu

        Comment savedComment = commentRepository.save(comment);

        post.getComments().add(savedComment);
        postRepository.save(post);

        return convertToDto(savedComment);
    }

    @Override
    public CommentDto findCommentById(Integer commentId) { // Hapus 'throws Exception'
        
        // [PERBAIKAN] Gunakan .orElseThrow dengan exception kustom
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new UrlNotFoundException("Comment not found with id: " + commentId));

        return convertToDto(comment);
    }

    @Override
    public CommentDto likeComment(Integer commentId, Integer userId) { // Hapus 'throws Exception'
        
        // [PERBAIKAN] Panggil helper internal yang sudah diperbaiki
        Comment comment = findCommentByIdInternal(commentId);
        
        User user = userService.findUserEntityById(userId); // Ini sudah aman

        if (!comment.getLiked().contains(user)) {
            comment.getLiked().add(user);
        } else {
            comment.getLiked().remove(user);
        }

        Comment savedComment = commentRepository.save(comment);
        return convertToDto(savedComment);
    }

    @Override
    public String deleteComment(Integer commentId, Integer userId) {
        
        // 1. Ambil entity User (yang meminta hapus)
        //    Ini sudah aman, akan melempar 'UserNotFound' jika tidak ada
        User requestingUser = userService.findUserEntityById(userId);

        // 2. Ambil entity Comment (yang akan dihapus)
        //    Menggunakan helper internal Anda yang sudah aman
        Comment comment = findCommentByIdInternal(commentId);

        // 3. [LOGIKA KEAMANAN]
        //    Verifikasi apakah user yang meminta adalah pemilik comment
        if (!comment.getUser().getId().equals(requestingUser.getId())) {
            // Jika bukan, lempar exception
            throw new ForbiddenException("You are not authorized to delete this comment.");
        }

        // 4. [LOGIKA KONSISTENSI DATA]
        //    Hapus comment dari daftar 'comments' di Post induknya
        //    Ini adalah kebalikan dari apa yang Anda lakukan di 'createComment'
        Post post = comment.getPost(); // Asumsi Comment entity punya getPost()
        if (post != null) {
            post.getComments().remove(comment);
            postRepository.save(post); // Simpan perubahan pada Post
        }

        // 5. Hapus comment dari repository
        commentRepository.delete(comment);

        return "Comment deleted successfully";
    }

    // --- HELPER METHODS ---

    /**
     * Helper internal untuk mengambil Entity Comment.
     */
    private Comment findCommentByIdInternal(Integer commentId) { // Hapus 'throws Exception'
        
        // [PERBAIKAN] Gunakan .orElseThrow dengan exception kustom
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new UrlNotFoundException("Comment not found with id: " + commentId));
    }

    /**
     * Helper method utama untuk konversi Comment (Entity) -> CommentDto (DTO).
     */
    private CommentDto convertToDto(Comment comment) {

        // [PERBAIKAN] Gunakan konverter DTO dari UserService agar konsisten
        UserDto userDto = userService.convertUserToDto(comment.getUser());

        // 2. Menghitung jumlah 'like' dari entity
        int likesCount = (comment.getLiked() != null) ? comment.getLiked().size() : 0;

        // 3. Buat dan isi CommentDto
        CommentDto commentDto = new CommentDto();

        commentDto.setId(comment.getId());
        commentDto.setContent(comment.getContent());
        commentDto.setCreatedAt(comment.getCreatedAt());
        commentDto.setUser(userDto); // Menggunakan DTO yang sudah dikonversi
        commentDto.setLikesCount(likesCount);

        return commentDto;
    }
}