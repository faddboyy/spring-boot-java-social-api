package com.faddboyy.socialapp.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.faddboyy.socialapp.dto.UserDto;
import com.faddboyy.socialapp.entities.User;
import com.faddboyy.socialapp.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private UserDto getAuthenticatedUserDto(Authentication auth) {
        String email = auth.getName(); 
        return userService.findUserByEmail(email);
    }

    @GetMapping
    public ResponseEntity<Page<UserDto>> searchOrFindAllUsers(
            @RequestParam(name = "search", required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        
        Page<UserDto> users = userService.findUsersPaginated(query, page, size);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("userId") Integer userId) {
        UserDto userDto = userService.findUserById(userId);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
    
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getMyProfile(Authentication auth) {
        UserDto userDto = getAuthenticatedUserDto(auth);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateMyProfile(
            @RequestBody User reqUser, 
            Authentication auth) {
        
        UserDto authUser = getAuthenticatedUserDto(auth);
        UserDto updatedUser = userService.updateUser(reqUser, authUser.getId());
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
    
    @PutMapping("/follow/{userId}")
    public ResponseEntity<UserDto> followUser(
            @PathVariable Integer userId,
            Authentication auth
    ) {
        UserDto authUser = getAuthenticatedUserDto(auth);
        UserDto followedUser = userService.followUser(authUser.getId(), userId);
        return new ResponseEntity<>(followedUser, HttpStatus.OK);
    }
}