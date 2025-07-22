package com.movieflix.exceptions;

public class NewPasswordTheSameAsOldException extends RuntimeException {
    public NewPasswordTheSameAsOldException(String message) {
        super(message);
    }
}
