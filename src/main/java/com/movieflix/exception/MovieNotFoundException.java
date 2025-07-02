package com.movieflix.exception;

public class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(Integer id) {
        super("Movie not found with id = " + id);
    }
}
