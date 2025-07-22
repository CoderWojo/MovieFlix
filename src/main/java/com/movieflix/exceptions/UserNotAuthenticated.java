package com.movieflix.exceptions;

public class UserNotAuthenticated extends RuntimeException {
    public UserNotAuthenticated(String message) {
        super(message);
    }
}
