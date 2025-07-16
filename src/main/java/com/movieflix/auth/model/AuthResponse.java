package com.movieflix.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {

//    odpowiedź na rejestrację

    private String accessToken;

    private String refreshToken;
}
