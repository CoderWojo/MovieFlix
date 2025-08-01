package com.movieflix.controllers;


import com.movieflix.auth.entities.User;
import com.movieflix.dto.VerifyEmailAndSendCodeRequest;
import com.movieflix.dto.MessageDto;
import com.movieflix.dto.VerificationRequest;
import com.movieflix.service.ForgotPasswordService;
import com.movieflix.utils.ChangePasswordRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/forgot-password")
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    public ForgotPasswordController(ForgotPasswordService forgotPasswordService) {
        this.forgotPasswordService = forgotPasswordService;
    }

    @PostMapping("/send-code")
    public ResponseEntity<MessageDto> verifyEmailAndSendCode(@RequestBody @Valid VerifyEmailAndSendCodeRequest request) {
        // 1. check if the user with given mail exists save ForgotPassword entity and send mail
        MessageDto message = forgotPasswordService.verifyEmailAndSendCode(request);

        return ResponseEntity.ok(message);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<MessageDto> verifyCode(@RequestBody @Valid VerificationRequest verificationRequest) {
        return ResponseEntity.ok(forgotPasswordService.verifyCode(verificationRequest));
    }

//    Jak coś to tylko jedna klasa może mieć @RequestBody
    @PostMapping("/change-password")
    public ResponseEntity<MessageDto> changePassword(@RequestBody ChangePasswordRequest passwords) {
        MessageDto message = forgotPasswordService.changePassword(passwords);

        return ResponseEntity.ok(message);
    }
}
