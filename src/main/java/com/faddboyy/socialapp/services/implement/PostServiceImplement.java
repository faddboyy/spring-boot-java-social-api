package com.faddboyy.socialapp.services.implement;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.faddboyy.socialapp.dto.PostDto;
import com.faddboyy.socialapp.dto.UserDto;
import com.faddboyy.socialapp.entities.Post;
import com.faddboyy.socialapp.entities.User;
import com.faddboyy.socialapp.exceptions.ForbiddenException;
import com.faddboyy.socialapp.exceptions.PostNotFoundException;
import com.faddboyy.socialapp.exceptions.UserNotFoundException;
import com.faddboyy.socialapp.repositories.PostRepository;
import com.faddboyy.socialapp.repositories.UserRepository;
import com.faddboyy.socialapp.services.PostService;
import com.faddboyy.socialapp.services.UserService;

@Service
@Transactional
public class PostServiceImplement implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public PostServiceImplement(PostRepository postRepository, UserRepository userRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public PostDto createNewPost(Post post, Integer userId) {
        User user = findUserEntityById(userId);

        Post newPost = new Post();
        newPost.setCaption(post.getCaption());
        newPost.setImage(post.getImage());
        newPost.setVideo(post.getVideo());
        newPost.setUser(user);
        newPost.setCreatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(newPost);
        return convertPostToDto(savedPost);
    }

    @Override
    public String deletePost(Integer postId, Integer userId) {
        Post post = findPostEntityById(postId);
        User user = findUserEntityById(userId);

        if (!post.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You can't delete another user's post");
        }

        postRepository.delete(post);
        return "Post deleted successfully";
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostDto> findPostsPaginated(String keyword, int page, int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage;

        if (keyword != null && !keyword.isBlank()) {
            postPage = postRepository.searchByCaption(keyword, pageable);
        } else {
            postPage = postRepository.findAll(pageable);
        }

        return postPage.map(this::convertPostToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto findPostById(Integer postId) {
        Post post = findPostEntityById(postId);
        return convertPostToDto(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostDto> findPostByUserIdPaginated(Integer userId, int page, int size) {
        findUserEntityById(userId); 
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findByUserId(userId, pageable);

        return postPage.map(this::convertPostToDto);
    }

    @Override
    public PostDto likePost(Integer postId, Integer userId) {
        Post post = findPostEntityById(postId);
        User user = findUserEntityById(userId);

        if (post.getLiked().contains(user)) {
            post.getLiked().remove(user);
        } else {
            post.getLiked().add(user);
        }
        Post savedPost = postRepository.save(post);
        return convertPostToDto(savedPost);
    }

    @Override
    public PostDto savedPost(Integer postId, Integer userId) {
        Post post = findPostEntityById(postId);
        User user = findUserEntityById(userId);

        if (user.getSavedPost().contains(post)) {
            user.getSavedPost().remove(post);
        } else {
            user.getSavedPost().add(post);
        }

        userRepository.save(user);
        Post refreshedPost = findPostEntityById(postId);

        return convertPostToDto(refreshedPost);
    }

    @Override
    @Transactional(readOnly = true)
    public Post findPostEntityById(Integer postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
    }

    @Transactional(readOnly = true)
    private User findUserEntityById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    private PostDto convertPostToDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setCaption(post.getCaption());
        postDto.setImage(post.getImage());
        postDto.setVideo(post.getVideo());
        postDto.setCreatedAt(post.getCreatedAt());

        UserDto userDto = userService.convertUserToDto(post.getUser());
        postDto.setUser(userDto);

        if (post.getLiked() != null) {
            postDto.setLikesCount(post.getLiked().size());
        } else {
            postDto.setLikesCount(0);
        }

        if (post.getSavedByUsers() != null) {
            postDto.setSaveCount(post.getSavedByUsers().size());
        } else {
            postDto.setSaveCount(0);
       }

        return postDto;
    }
}