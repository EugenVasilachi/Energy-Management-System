package com.example.user.dtos.builders;

import com.example.user.dtos.UserDTO;
import com.example.user.entities.User;

public class UserBuilder {

    private UserBuilder() {
    }

    public static UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getUsername(), user.getPassword(), user.getRole());
    }

    public static User toEntity(UserDTO userDTO) {
        return new User(userDTO.getName(),
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getRole());
    }


}
