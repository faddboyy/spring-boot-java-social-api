package com.faddboyy.socialapp.services;

import com.faddboyy.socialapp.dto.CommentDto;
import com.faddboyy.socialapp.entities.Comment;

// Interface ini mendefinisikan "kontrak" yang diharapkan oleh Controller
public interface CommentService {

    /**
     * Membuat komentar baru dan mengembalikannya sebagai DTO.
     */
    CommentDto createComment(Comment comment, Integer postId, Integer userId) throws Exception;

    /**
     * Menemukan komentar berdasarkan ID dan mengembalikannya sebagai DTO.
     */
    CommentDto findCommentById(Integer commentId) throws Exception;

    /**
     * Like/unlike komentar dan mengembalikan DTO yang sudah di-update.
     */
    CommentDto likeComment(Integer commentId, Integer userId) throws Exception;

    /**
     * Menghapus sebuah comment.
     * Hanya bisa dilakukan oleh user yang membuat comment tsb.
     * @param commentId ID comment yang akan dihapus
     * @param userId ID user yang meminta delete (dari Auth)
     * @return Pesan sukses
     */
    String deleteComment(Integer commentId, Integer userId);
}