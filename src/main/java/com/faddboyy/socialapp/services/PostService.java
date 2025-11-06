package com.faddboyy.socialapp.services;

import org.springframework.data.domain.Page;
import com.faddboyy.socialapp.dto.PostDto;
import com.faddboyy.socialapp.entities.Post;

public interface PostService {

    PostDto createNewPost(Post post, Integer userId);

    String deletePost(Integer postId, Integer userId);

    Page<PostDto> findPostsPaginated(String keyword, int page, int size);

    PostDto findPostById(Integer postId);

    Page<PostDto> findPostByUserIdPaginated(Integer userId, int page, int size);

    PostDto likePost(Integer postId, Integer userId);

    PostDto savedPost(Integer postId, Integer userId);

    Post findPostEntityById(Integer postId);
}