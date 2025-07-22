package com.movieflix.exceptions;

public class ForgotPasswordNotFound extends RuntimeException {
    public ForgotPasswordNotFound() {
        super("Forgot Password is not found.");
    }
}
