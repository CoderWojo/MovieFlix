package com.movieflix.controllers;

import com.movieflix.auth.entities.RefreshToken;
import com.movieflix.auth.entities.User;
import com.movieflix.auth.dto.AuthResponse;
import com.movieflix.auth.dto.LoginRequest;
import com.movieflix.auth.dto.RefreshTokenRequest;
import com.movieflix.auth.dto.RegisterRequest;
import com.movieflix.auth.services.AuthService;
import com.movieflix.auth.services.JwtService;
import com.movieflix.auth.services.RefreshTokenService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;


    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest registerRequest, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(authService.register(registerRequest, response));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(loginRequest, response));
    }

//    ten endpoint wywołujemy bez jwt , no bo przecież właśnie on jest już wygasły
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody @Valid RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(refreshTokenRequest.getRefreshToken());
        User user = refreshToken.getUser();
        //            zwracamy sam, odnowiony accessToken
//            Nowy refreshTOken wraz z access dostaje się po udanej rejestracji/logowaniu
        String accessToken = jwtService.generateToken(user.getUsername());

        return ResponseEntity.ok(AuthResponse.builder()
                .accessToken(accessToken)
//                .refreshToken(refreshTokenService.generateRefreshToken(user).getToken())
                .build());

    }
}
