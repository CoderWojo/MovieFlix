package com.movieflix.mapper;

import com.movieflix.auth.entities.User;
import com.movieflix.dto.VerifyEmailAndSendCodeRequest;
import com.movieflix.entities.ForgotPassword;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ForgotPasswordMapper {

    public ForgotPassword forgotPasswordDtoToForgotPassword(Integer otp, User user) {
        LocalDateTime expTime = LocalDateTime.now().plusMinutes(10);

        return ForgotPassword.builder()
                .otp(otp)
                .user(user)
                .expirationTime(expTime)
                .build();
    }
}
