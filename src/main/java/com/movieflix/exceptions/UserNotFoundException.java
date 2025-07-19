package com.movieflix.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("We don't have registered user with the given email.");
    }
}
