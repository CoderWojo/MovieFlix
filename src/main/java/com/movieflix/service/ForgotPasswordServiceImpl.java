package com.movieflix.service;

import com.movieflix.auth.entities.User;
import com.movieflix.auth.repositories.UserRepository;
import com.movieflix.dto.VerifyEmailAndSendCodeRequest;
import com.movieflix.dto.MailBody;
import com.movieflix.dto.MessageDto;
import com.movieflix.dto.VerificationRequest;
import com.movieflix.entities.ForgotPassword;
import com.movieflix.exceptions.*;
import com.movieflix.mapper.ForgotPasswordMapper;
import com.movieflix.repository.ForgotPasswordRepository;
import com.movieflix.utils.ChangePasswordRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ForgotPasswordMapper mapper;
    private final ForgotPasswordRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Value("${project.mail.subject}")
    private String subject;

    @Value("${forgot_password.allowed_attempts}")
    private int allowed_attempts;

    public ForgotPasswordServiceImpl(UserRepository userRepository, EmailService emailService, ForgotPasswordMapper mapper, ForgotPasswordRepository repository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.mapper = mapper;
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

//    1. step - checking if user with given mail already exists and 1.1. - sending verification code

    @Override
    @Transactional // bo nie chcemy aby zapisal sie kod w bazie a userowi nie wyslal mail
    public MessageDto verifyEmailAndSendCode(VerifyEmailAndSendCodeRequest forgotPassDto) {

        String email =  forgotPassDto.email();
        User u = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
//        sprawdź czy nie istenije już jakiś kod w bazie dla tego użytkownika
        Optional<ForgotPassword> OpForgotPassword = repository.findByUser(u);
        // kasujemy stary forgotPassword
        OpForgotPassword.ifPresent(repository::delete);

// wymusza delete od razu a nie przed samym zamknieciem transakcji, bo wtedy były jednoczesnie 2 takie same forgotPassword z tym samym user_id jesli ktos 2. raz resetowal haslo
        repository.flush();

        Integer otp = generateOtp();

        // zapisz ForgotPassword w bazie
        // note that expirationTime is set in mapper class
        ForgotPassword forgotPassword = mapper.forgotPasswordDtoToForgotPassword(forgotPassDto, otp, u);
        ForgotPassword saved = repository.save(forgotPassword);

        MailBody mailBody = MailBody.builder()
                .to(forgotPassDto.email())  // okreslamy na poziomie ForgotPasswordService
                .subject(subject)   // też ^
                .text("This is the one-time-password for your Forgot password request: " + otp)
                .build();

// przyjmuje 3 param. których nie znamy ot tak w EmailService
        emailService.sendSimpleMailMessage(mailBody);

        return new MessageDto("Verification code is sent to your email successfully!", true);
    }

    private Integer generateOtp() {
        return ThreadLocalRandom.current().nextInt(100_000, 999_999);
    }

    // 2. step - verify the code ( front-end wyśle żądanie z tymi parametrami, tzn. zapamięta email z poprzedniego endpointu)
    public MessageDto verifyCode(VerificationRequest verificationRequest) { // email, code
        String email = verificationRequest.email();
        Integer code = verificationRequest.code();

        User user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        int user_id = user.getId();
        ForgotPassword expectedForgotPassword = repository.findByUser(user)
                .orElseThrow(ExpectedForgotPasswordNotFound::new); // raczej niemozliwe, no ale ktos moglby wyslac taki request

        Integer expectedOtp = expectedForgotPassword.getOtp();
        int attempts = expectedForgotPassword.getAttempts();

        if(expectedForgotPassword.isUsed()) {   // U(used)
            throw new CodeAlreadyUsedException("The code had been used by someone else.");
        } else if(expectedForgotPassword.getExpirationTime().isBefore(LocalDateTime.now())) {   // E(expired)
            throw new ExpiredOtpException();
        } else if(attempts >= allowed_attempts) {  // A(attempts)
            throw new TooManyAttemptsException("Too many attempts. Generate another otp.");
        } else if(!code.equals(expectedOtp)) {  // C(code)
            attempts += 1;
            // update attempts column
            repository.updateAttemptsByUserId(attempts, user_id);
            throw new NotTheSameOtpException("Please try again.");
        }

        repository.updateUsedByUserId(true, user_id);

        var now = LocalDateTime.now();
        attempts += 1;
        repository.updateVerifiedAtByUserId(now, user_id);
        repository.updateAttemptsByUserId(attempts, user_id);
        return new MessageDto("Verification went successfully.", true);
    }

    @Override
    @Transactional
    public MessageDto changePassword(ChangePasswordRequest request) {   // email, pass1, pass2, otp
        Integer code = request.code();
        String email = request.email();

        String newPassword = request.password();
        String repeatedPass = request.repeatPassword();
        User u = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

//        w dodatku sprawdzamy czy nie wygasł bo ktos moglby skonczyc na wyslaniu kodu ale nie wyslaniu
        ForgotPassword fp = repository.findByUserAndOtp(u, code)
                .orElseThrow(ForgotPasswordNotFound::new);
        if(fp.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new ExpiredOtpException();
        }

        if(!newPassword.equals(repeatedPass)) {
            throw new RepeatedPasswordNotTheSameAsNew("Passwords are not the same! Please correct them!");
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        u.setPassword(encodedNewPassword);
        int updatedRecords = userRepository.updatePassword(email, encodedNewPassword);

        return new MessageDto("Updated: " + updatedRecords + " records.", true);
    }
}
