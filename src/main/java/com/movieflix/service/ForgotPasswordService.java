package com.movieflix.service;

import com.movieflix.dto.ForgotPasswordDto;

public interface ForgotPasswordService {

    String verifyEmailAndSendCode(ForgotPasswordDto forgotPassDto);
}
