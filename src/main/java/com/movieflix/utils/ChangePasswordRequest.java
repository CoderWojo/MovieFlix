package com.movieflix.utils;

public record ChangePasswordRequest(String email, String password, String repeatPassword, Integer code) {

}
