package com.movieflix.service;

import com.movieflix.dto.VerifyEmailAndSendCodeRequest;
import com.movieflix.dto.MessageDto;
import com.movieflix.dto.VerificationRequest;
import com.movieflix.utils.ChangePasswordRequest;

public interface ForgotPasswordService {

    MessageDto verifyEmailAndSendCode(VerifyEmailAndSendCodeRequest forgotPassDto);

    MessageDto verifyCode(VerificationRequest verificationRequest);

    MessageDto changePassword(ChangePasswordRequest passwords);
}
