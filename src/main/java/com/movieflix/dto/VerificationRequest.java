package com.movieflix.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerificationRequest(@NotBlank Integer code, @NotBlank @Email String email) {
}
