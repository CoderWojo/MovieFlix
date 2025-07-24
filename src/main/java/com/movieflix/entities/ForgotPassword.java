package com.movieflix.entities;

import com.movieflix.auth.entities.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
public class ForgotPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private Integer otp;

    @NotNull
    private LocalDateTime expirationTime;

    @OneToOne
    @JoinColumn
    private User user;

    @NotNull
    private int attempts;

    @NotNull
    private boolean used;

    @Nullable
    private LocalDateTime verifiedAt;   // Hibernate automatycznie konwertuje nazwy pól z Java na sneak_case
}
