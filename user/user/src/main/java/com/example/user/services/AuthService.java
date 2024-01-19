package com.example.user.services;

import com.example.user.dtos.UserDTO;
import com.example.user.dtos.builders.UserBuilder;
import com.example.user.entities.User;
import com.example.user.repositories.UserRepository;
import com.example.user.response.LoginMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginMessage save(UserDTO userDTO) {
        // Check if a user with the same username already exists
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            return new LoginMessage("Username already exists", false, userDTO.getRole(), userDTO.getId());
        }
        User user = UserBuilder.toEntity(userDTO);
        user = userRepository.save(user);
        return new LoginMessage("Sign Up Success", true, userDTO.getRole(), user.getId());
    }

    public User findUserByUsername(String username) {

        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> null);
    }

}
