package com.example.user.controllers;

import com.example.user.config.JwtUtil;
import com.example.user.dtos.LoginReqDTO;
import com.example.user.dtos.UserDTO;
import com.example.user.dtos.builders.UserBuilder;
import com.example.user.entities.User;
import com.example.user.response.ErrorRes;
import com.example.user.response.LoginMessage;
import com.example.user.response.LoginResDTO;
import com.example.user.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value="/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthService authService,
                          AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {
        LoginMessage loginResponse = authService.save(userDTO);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginReqDTO loginReqDTO) {

        LoginResDTO loginResDTO;
        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                            loginReqDTO.getUsername(), loginReqDTO.getPassword()
                    ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String username = authentication.getName();

            // if findUserByUsername is called we are sure that user exists,
            // otherwise authenticate would have thrown an exception
            User user = authService.findUserByUsername(username);
            UserDTO userDTO = UserBuilder.toUserDTO(user);
            String token = jwtUtil.createToken(userDTO);
            loginResDTO = new LoginResDTO(username, token);

            return ResponseEntity.ok(loginResDTO);

        } catch (BadCredentialsException e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST,"Invalid username or password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e){
            ErrorRes errorResponse = new ErrorRes(HttpStatus.BAD_REQUEST, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }


}
