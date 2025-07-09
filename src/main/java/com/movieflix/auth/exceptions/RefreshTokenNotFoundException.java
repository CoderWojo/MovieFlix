package com.movieflix.auth.exceptions;

public class RefreshTokenNotFoundException extends RuntimeException {
    public RefreshTokenNotFoundException(String token) {
        super("Refresh token not found with token = " + token);
    }
}
