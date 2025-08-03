package com.movieflix.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VerificationRequest(@NotNull Integer code, @NotBlank @Email String email) {
}
