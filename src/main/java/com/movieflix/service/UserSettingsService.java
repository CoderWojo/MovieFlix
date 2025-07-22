package com.movieflix.service;

import com.movieflix.dto.MessageDto;
import com.movieflix.utils.ChangePasswordByOwnRequest;

public interface UserSettingsService {

    MessageDto changePasswordByOwn(ChangePasswordByOwnRequest request);
}
