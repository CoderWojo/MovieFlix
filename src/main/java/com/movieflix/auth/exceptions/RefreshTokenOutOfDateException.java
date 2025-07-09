package com.movieflix.auth.exceptions;

public class RefreshTokenOutOfDateException extends RuntimeException {
    public RefreshTokenOutOfDateException(String token) {
        super("Refresh token: " + token + " is out of date.");
    }
}
