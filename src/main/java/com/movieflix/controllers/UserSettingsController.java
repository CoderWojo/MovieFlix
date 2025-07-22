package com.movieflix.controllers;

import com.movieflix.dto.MessageDto;
import com.movieflix.service.UserSettingsService;
import com.movieflix.utils.ChangePasswordByOwnRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/settings")
public class UserSettingsController {

    private final UserSettingsService userSettingsService;

    public UserSettingsController(UserSettingsService userSettingsService) {
        this.userSettingsService = userSettingsService;
    }

    @PostMapping("/change-password")
    public ResponseEntity<MessageDto> changePasswordByOwn(@RequestBody ChangePasswordByOwnRequest request) {
        MessageDto message = userSettingsService.changePasswordByOwn(request);

        return ResponseEntity.ok(message);
    }
}
