package com.movieflix.exceptions;

public class NotTheSamePasswordException extends RuntimeException {
    public NotTheSamePasswordException(String message) {
        super(message);
    }
}
