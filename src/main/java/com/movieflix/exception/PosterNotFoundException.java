package com.movieflix.exception;

import java.io.FileNotFoundException;

public class PosterNotFoundException extends FileNotFoundException {
    public PosterNotFoundException(String message) {
        super(message);
    }
}