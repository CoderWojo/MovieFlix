package com.movieflix.dto;

import lombok.Builder;

@Builder
public class UserDto {

    public UserDto(String username, String role, String email) {
        this.username = username;
        this.role = role;
        this.email = email;
    }

    private String username;

    private String email;

    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
