package com.example.user.response;

import com.example.user.entities.UserRole;

import java.util.UUID;

public class LoginMessage {
    String message;
    Boolean status;
    UserRole role;
    UUID idUser;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Boolean getStatus() {
        return status;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }

    public UUID getIdUser() {
        return idUser;
    }

    public void setIdUser(UUID idUser) {
        this.idUser = idUser;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public LoginMessage(String message, boolean status, UserRole role, UUID idUser) {
        this.message = message;
        this.status = status;
        this.role = role;
        this.idUser = idUser;
    }
}
