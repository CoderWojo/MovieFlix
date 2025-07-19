package com.movieflix.service;

import com.movieflix.auth.entities.User;
import com.movieflix.auth.repositories.UserRepository;
import com.movieflix.dto.ForgotPasswordDto;
import com.movieflix.dto.MailBody;
import com.movieflix.entities.ForgotPassword;
import com.movieflix.exceptions.UserNotFoundException;
import com.movieflix.mapper.ForgotPasswordMapper;
import com.movieflix.repository.ForgotPasswordRepository;
import org.springframework.beans.factory.annotation.Value;

import java.util.Random;

public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ForgotPasswordMapper mapper;
    private final ForgotPasswordRepository repository;

    @Value("${project.mail.subject}")
    private String subject;

    public ForgotPasswordServiceImpl(UserRepository userRepository, EmailService emailService, ForgotPasswordMapper mapper, ForgotPasswordRepository repository) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.mapper = mapper;
        this.repository = repository;
    }

//    1. step - checking if user with given mail already exists and 1.1. - sending verification code
    @Override
    public String verifyEmailAndSendCode(ForgotPasswordDto forgotPassDto) {

        String email =  forgotPassDto.email();
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        Integer otp = generateOtp();

        // zapisz ForgotPassword w bazie
        // note that expirationTime is set in mapper class
        ForgotPassword forgotPassword = mapper.forgotPasswordDtoToForgotPassword(forgotPassDto, otp, user);
        ForgotPassword saved = repository.save(forgotPassword);
        MailBody mailBody = MailBody.builder()
                .to(forgotPassDto.email())  // okreslamy na poziomie ForgotPasswordService
                .subject(subject)   // też ^
                .text("This is the one-time-password for your Forgot password request: " + otp)
                .build();

// przyjmuje 3 param. których nie znamy ot tak
        emailService.sendSimpleMailMessage(mailBody);

        return "Verification code is sent to your email successfully!";
    }

    private Integer generateOtp() {
        Random random = new Random(1000);
        return random.nextInt(100_000, 999_999);
    }

    // 2. step - verify the code
}
