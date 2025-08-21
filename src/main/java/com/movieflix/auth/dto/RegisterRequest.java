package com.movieflix.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank
    private String username;

    @Email
    private String email;

    @Size(min = 5, message = "Hasło musi mieć co najmniej 5 znaków.")
    private String password;

    @NotBlank
    private String repeat;
}
