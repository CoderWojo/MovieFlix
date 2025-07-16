package com.movieflix.auth.services;

import com.movieflix.auth.model.AuthResponse;
import com.movieflix.auth.model.LoginRequest;
import com.movieflix.auth.model.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest);

    AuthResponse login(LoginRequest loginRequest);
}