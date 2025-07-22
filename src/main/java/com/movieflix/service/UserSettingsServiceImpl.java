package com.movieflix.service;

import com.movieflix.auth.entities.User;
import com.movieflix.auth.repositories.UserRepository;
import com.movieflix.dto.MessageDto;
import com.movieflix.exceptions.*;
import com.movieflix.utils.ChangePasswordByOwnRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserSettingsServiceImpl implements UserSettingsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSettingsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public MessageDto changePasswordByOwn(ChangePasswordByOwnRequest request) {
        String oldPasswordRequest = request.oldPassword();  // not encoded
        String password = request.newPassword();
        String repeated = request.repeatPassword();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 1. check if the user is authenticated
        if(!auth.isAuthenticated()) {
            throw new UserNotAuthenticated("Please authorize!");
        }

//        2. get the user from authentication in order to read the User by userRepository
        User user = (User) auth.getPrincipal();
        String actualPassword = user.getPassword(); // encoded
        String username = user.getUsername();
//        3. check correct of oldPassword in order to dodge forgery

        if(!passwordEncoder.matches(oldPasswordRequest, actualPassword)) {
            throw new NotTheSameOldPasswordException("Please enter correct actual password!");
        }

        // 4. check passwords equality
        if(!password.equals(repeated)) {
            throw new NotTheSamePasswordException("Passwords must be the same!");
        }

//         5. check if the new password is not the same as old
        if(oldPasswordRequest.equals(password)) {
            throw new NewPasswordTheSameAsOldException("New password must be diffrent from the old.");
        }

        String encoded = passwordEncoder.encode(password);

        // 5. invoke the query
        int updatedRows = userRepository.updatePasswordByOwn(username, encoded);

        return new MessageDto("Your password has been changed.", true);
    }
}
