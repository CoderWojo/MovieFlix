package com.movieflix.exceptions;

public class NotTheSameOldPasswordException extends RuntimeException {
    public NotTheSameOldPasswordException(String s) {
        super(s);
    }
}
