package com.movieflix.auth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500)
    @NotBlank(message = "Please enter a refresh token value!")
    private String refreshToken;

    @Column(nullable = false)
    private Instant expirationTime;

//    token zależy od usera, więc FK dajemy w RefreshToken
    @OneToOne
    @JoinColumn
    private User user;
}
