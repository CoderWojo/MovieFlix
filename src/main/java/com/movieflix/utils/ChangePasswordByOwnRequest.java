package com.movieflix.utils;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordByOwnRequest(

        @NotBlank
        String oldPassword,

        @Size(min = 5)
        String newPassword,

        @NotBlank
        String repeatPassword) {
}
