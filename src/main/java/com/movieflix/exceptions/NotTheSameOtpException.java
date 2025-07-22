package com.movieflix.exceptions;

public class NotTheSameOtpException extends RuntimeException {
    public NotTheSameOtpException(String message) {
        super(message);
    }
}
