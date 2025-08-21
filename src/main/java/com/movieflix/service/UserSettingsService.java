package com.movieflix.service;

import com.movieflix.dto.MessageDto;
import com.movieflix.dto.UserDto;
import com.movieflix.utils.ChangePasswordByOwnRequest;
import org.springframework.security.core.Authentication;

public interface UserSettingsService {

    MessageDto changePasswordByOwn(ChangePasswordByOwnRequest request);

    UserDto me(Authentication authentication);
}
