package com.movieflix.dto;

import jakarta.validation.constraints.NotBlank;

public record VerifyEmailAndSendCodeRequest(@NotBlank String email) {
}
