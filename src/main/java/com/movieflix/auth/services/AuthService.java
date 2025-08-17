package com.movieflix.auth.services;

import com.movieflix.auth.dto.AuthResponse;
import com.movieflix.auth.dto.LoginRequest;
import com.movieflix.auth.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest, HttpServletResponse response);

    AuthResponse login(LoginRequest loginRequest, HttpServletResponse response);


}