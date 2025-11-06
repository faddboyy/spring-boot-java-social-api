package com.faddboyy.socialapp.services.implement;

import com.faddboyy.socialapp.dto.UserDto;
import com.faddboyy.socialapp.entities.User;
import com.faddboyy.socialapp.exceptions.BadRequestException;
import com.faddboyy.socialapp.exceptions.EmailAlreadyExistsException;
import com.faddboyy.socialapp.exceptions.UserNotFoundException;
import com.faddboyy.socialapp.repositories.UserRepository;
import com.faddboyy.socialapp.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImplement implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImplement(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto registerUser(User reqUser) {

        Optional<User> isEmailExist = userRepository.findByEmail(reqUser.getEmail());
        if (isEmailExist.isPresent()) {
            throw new EmailAlreadyExistsException("Email '" + reqUser.getEmail() + "' is already taken.");
        }

        User newUser = new User();
        newUser.setFirstName(reqUser.getFirstName());
        newUser.setLastName(reqUser.getLastName());
        newUser.setEmail(reqUser.getEmail());
        newUser.setPassword(passwordEncoder.encode(reqUser.getPassword()));
        newUser.setGender(reqUser.getGender());

        try {
            User savedUser = userRepository.save(newUser);
            return convertUserToDto(savedUser);

        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistsException(
                    "Database error: Email '" + reqUser.getEmail() + "' might already exist.");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not exist with email: " + email));

        return convertUserToDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto findUserById(Integer userId) {
        User user = findUserEntityById(userId);
        return convertUserToDto(user);
    }

    @Override
    public UserDto followUser(Integer userId1, Integer userId2) {
        if (userId1.equals(userId2)) {
            throw new BadRequestException("You cannot follow yourself");
        }
        User user1 = findUserEntityById(userId1);
        User user2 = findUserEntityById(userId2);

        if (user1.getFollowings().contains(user2)) {
            user1.getFollowings().remove(user2);
            user2.getFollowers().remove(user1);
        } else {
            user1.getFollowings().add(user2);
            user2.getFollowers().add(user1);
        }
        userRepository.save(user1);
        userRepository.save(user2);

        return convertUserToDto(user2);
    }

    @Override
    public UserDto updateUser(User reqUser, Integer userId) {
        User oldUser = findUserEntityById(userId);

        if (reqUser.getFirstName() != null)
            oldUser.setFirstName(reqUser.getFirstName());
        if (reqUser.getLastName() != null)
            oldUser.setLastName(reqUser.getLastName());

        if (reqUser.getEmail() != null && !reqUser.getEmail().equals(oldUser.getEmail())) {
            Optional<User> isEmailExist = userRepository.findByEmail(reqUser.getEmail());
            if (isEmailExist.isPresent()) {
                throw new EmailAlreadyExistsException("Email '" + reqUser.getEmail() + "' is already taken.");
            }
            oldUser.setEmail(reqUser.getEmail());
        }

        if (reqUser.getPassword() != null && !reqUser.getPassword().isEmpty()) {
            oldUser.setPassword(passwordEncoder.encode(reqUser.getPassword()));
        }
        if (reqUser.getGender() != null)
            oldUser.setGender(reqUser.getGender());

        User updatedUser = userRepository.save(oldUser);
        return convertUserToDto(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> findUsersPaginated(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage;

        if (query != null && !query.isBlank()) {
            userPage = userRepository.searchUser(query, pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        return userPage.map(this::convertUserToDto);
    }

    @Override
    public String deleteUser(Integer userId) {
        User user = findUserEntityById(userId);
        userRepository.delete(user);
        return "User deleted successfully";
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto convertUserToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setGender(user.getGender());

        if (user.getFollowers() != null) {
            userDto.setFollowerCount(user.getFollowers().size());
        } else {
            userDto.setFollowerCount(0);
        }

        if (user.getFollowings() != null) {
            userDto.setFollowingCount(user.getFollowings().size());
        } else {
            userDto.setFollowingCount(0);
        }

        if (user.getPosts() != null) {
            userDto.setPostCount(user.getPosts().size());
        } else {
            userDto.setPostCount(0);
        }

        return userDto;
    }

    @Override
    @Transactional(readOnly = true)
    public User findUserEntityById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not exist with id: " + userId));
    }
}