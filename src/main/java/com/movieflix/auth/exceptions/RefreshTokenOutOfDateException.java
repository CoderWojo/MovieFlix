package com.movieflix.auth.exceptions;

public class RefreshTokenOutOfDateException extends RuntimeException {
    public RefreshTokenOutOfDateException() {
        super("Refresh token: *** is out of date.");
    }
}
