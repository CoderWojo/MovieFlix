package com.movieflix.exceptions;

public class CodeAlreadyUsedException extends RuntimeException {
    public CodeAlreadyUsedException(String message) {
        super(message);
    }
}
