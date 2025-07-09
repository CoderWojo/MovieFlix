package com.movieflix.exceptions;

public class MovieExistsException extends RuntimeException {

    public MovieExistsException(Integer id) {
        super("Movie with id = " + id + " already exists! Please write the correct ID!");
    }
}
