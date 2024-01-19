package com.example.user.controllers;

import com.example.user.controllers.handlers.exceptions.ResourceNotFoundException;
import com.example.user.dtos.UserDTO;
import com.example.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value="/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> dtos = userService.findUsers();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<UUID> createUser(@RequestBody UserDTO userDTO) {
        UUID userID = userService.save(userDTO);
        return new ResponseEntity<>(userID, HttpStatus.CREATED);
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") UUID userID) {
        UserDTO dto = userService.findUserById(userID);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<UUID> deleteUser(@PathVariable("id") UUID userID) {
        userService.deleteUserById(userID);

        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://172.16.238.6:8081/userdevices/deleteFromUser/{id}";
        restTemplate.delete(apiUrl, userID);

        return new ResponseEntity<>(userID, HttpStatus.NO_CONTENT);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") UUID userID, @RequestBody UserDTO newUserDTO) {
        try {
            UserDTO updateDTO = userService.updateUserById(userID, newUserDTO);
            return ResponseEntity.ok(updateDTO);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request");
        }
    }

}
