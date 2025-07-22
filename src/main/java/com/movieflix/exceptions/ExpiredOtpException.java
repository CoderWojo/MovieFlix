package com.movieflix.exceptions;

public class ExpiredOtpException extends RuntimeException {
    public ExpiredOtpException() {
        super("Your otp is expired.");
    }
}
