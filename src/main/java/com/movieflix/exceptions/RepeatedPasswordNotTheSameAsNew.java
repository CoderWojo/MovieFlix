package com.movieflix.exceptions;

public class RepeatedPasswordNotTheSameAsNew extends RuntimeException {
  public RepeatedPasswordNotTheSameAsNew(String message) {
    super(message);
  }
}
