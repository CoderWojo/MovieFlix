package com.movieflix.controllers;

import com.movieflix.dto.MessageDto;
import com.movieflix.service.UserSettingsService;
import com.movieflix.utils.ChangePasswordByOwnRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/settings")
public class UserController {

    private final UserSettingsService userSettingsService;

    public UserController(UserSettingsService userSettingsService) {
        this.userSettingsService = userSettingsService;
    }

    @PostMapping("/change-password")
    public ResponseEntity<MessageDto> changePasswordByOwn(@RequestBody ChangePasswordByOwnRequest request) {
        MessageDto message = userSettingsService.changePasswordByOwn(request);

        return ResponseEntity.ok(message);
    }

    @GetMapping("/me")
//    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageDto> me(Authentication authentication) {
        return ResponseEntity.ok(new MessageDto(userSettingsService.me(authentication).toString(), true));
    }
}
