package com.movieflix.auth.mappers;

import com.movieflix.auth.entities.User;
import com.movieflix.auth.entities.UserRole;
import com.movieflix.auth.model.RegisterRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    private final PasswordEncoder passwordEncoder;

    public AuthMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User registerRequestToUser(RegisterRequest registerRequest) {
        return User.builder()
                .email(registerRequest.getEmail())
                .role(UserRole.USER)
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .username(registerRequest.getUsername())
                .build();
    }
}
