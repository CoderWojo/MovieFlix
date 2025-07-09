package com.movieflix.auth.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Entity
@Builder
@Getter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 500)
    @NotBlank(message = "Please enter a refresh token value!")
    private String token;

    @Column(nullable = false)
    private Instant expirationTime;

//    token zależy od usera, więc FK dajemy w RefreshToken
    @ManyToOne
    @JoinColumn
    private User user;
}
