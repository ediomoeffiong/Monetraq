package com.trasexp.presentation.controller;

import com.trasexp.application.service.AuthenticationHelper;
import com.trasexp.application.service.SettingsService;
import com.trasexp.presentation.dto.NotificationPreferencesDTO;
import com.trasexp.presentation.dto.UserPreferencesDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingsController {

    private final SettingsService settingsService;
    private final AuthenticationHelper authenticationHelper;

    @GetMapping("/notifications")
    public ResponseEntity<NotificationPreferencesDTO> getNotificationPreferences(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        NotificationPreferencesDTO prefs = settingsService.getNotificationPreferences(userId);
        return ResponseEntity.ok(prefs);
    }

    @PutMapping("/notifications")
    public ResponseEntity<NotificationPreferencesDTO> updateNotificationPreferences(
            @RequestBody NotificationPreferencesDTO dto,
            Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        NotificationPreferencesDTO updated = settingsService.updateNotificationPreferences(userId, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/preferences")
    public ResponseEntity<UserPreferencesDTO> getUserPreferences(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        UserPreferencesDTO prefs = settingsService.getUserPreferences(userId);
        return ResponseEntity.ok(prefs);
    }

    @PutMapping("/preferences")
    public ResponseEntity<UserPreferencesDTO> updateUserPreferences(
            @RequestBody UserPreferencesDTO dto,
            Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        UserPreferencesDTO updated = settingsService.updateUserPreferences(userId, dto);
        return ResponseEntity.ok(updated);
    }

    private Long getUserIdFromAuth(Authentication authentication) {
        return authenticationHelper.getUserIdFromAuthentication(authentication);
    }
}

