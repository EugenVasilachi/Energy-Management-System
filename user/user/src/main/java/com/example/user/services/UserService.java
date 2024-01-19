package com.example.user.services;

import com.example.user.controllers.handlers.exceptions.ResourceNotFoundException;
import com.example.user.dtos.UserDTO;
import com.example.user.dtos.builders.UserBuilder;
import com.example.user.entities.User;
import com.example.user.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> findUsers() {
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .map(UserBuilder::toUserDTO)
                .collect(Collectors.toList());
    }

    public UUID save(UserDTO userDTO) {
        User user = UserBuilder.toEntity(userDTO);
        user = userRepository.save(user);
        LOGGER.debug("User with id {} was inserted in db", user.getId());
        return user.getId();
    }

    public UserDTO findUserById(UUID userID) {
        Optional<User> prosumerOptional = userRepository.findById(userID);
        if (prosumerOptional.isEmpty()) {
            LOGGER.error("User with id {} was not found in db", userID);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + userID);
        }
        return UserBuilder.toUserDTO(prosumerOptional.get());
    }

    public void deleteUserById(UUID userID) {
        Optional<User> prosumerOptional = userRepository.findById(userID);
        if(prosumerOptional.isEmpty()) {
            LOGGER.error("User with id {} was not found in db", userID);
            throw new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + userID);
        }
        userRepository.deleteById(userID);
    }

    public UserDTO updateUserById(UUID userID, UserDTO newUserDTO) {
        User updatedUser =  userRepository.findById(userID)
                .map(user -> {
                    user.setName(newUserDTO.getName());
                    user.setUsername(newUserDTO.getUsername());
                    user.setPassword(newUserDTO.getPassword());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName() + " with id: " + userID));

        return UserBuilder.toUserDTO(updatedUser);
    }

}
