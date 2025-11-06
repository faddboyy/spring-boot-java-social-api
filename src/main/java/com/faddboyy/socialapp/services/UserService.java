package com.faddboyy.socialapp.services;

import com.faddboyy.socialapp.dto.UserDto;
import com.faddboyy.socialapp.entities.User;
import org.springframework.data.domain.Page;

public interface UserService {

    UserDto registerUser(User reqUser);

    UserDto findUserByEmail(String email);

    UserDto findUserById(Integer userId);

    UserDto followUser(Integer userId1, Integer userId2);

    UserDto updateUser(User reqUser, Integer userId);

    Page<UserDto> findUsersPaginated(String query, int page, int size);

    String deleteUser(Integer userId);

    UserDto convertUserToDto(User user);

    User findUserEntityById(Integer userId);
}