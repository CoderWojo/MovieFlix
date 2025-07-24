package com.movieflix.exceptions;

public class TooManyAttemptsException extends RuntimeException {
  public TooManyAttemptsException(String message) {
    super(message);
  }
}
