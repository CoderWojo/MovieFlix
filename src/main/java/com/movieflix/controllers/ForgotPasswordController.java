package com.movieflix.controllers;


import com.movieflix.dto.ForgotPasswordDto;
import com.movieflix.service.ForgotPasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/forgot-password")
public class ForgotPasswordController {

    private ForgotPasswordService forgotPasswordService;

    @PostMapping("/verifyMail")
    public ResponseEntity<String> verifyEmail(ForgotPasswordDto forgotPassDto) {
        // 1. check if the user with given mail exists save ForgotPassword entity and send mail
        String message = forgotPasswordService.verifyEmailAndSendCode(forgotPassDto);

        return ResponseEntity.ok(message);
    }
}
