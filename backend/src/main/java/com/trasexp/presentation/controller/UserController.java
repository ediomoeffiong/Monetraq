package com.trasexp.presentation.controller;

import com.trasexp.application.service.AuthenticationHelper;
import com.trasexp.application.service.UserService;
import com.trasexp.presentation.dto.AccountActionRequest;
import com.trasexp.presentation.dto.ChangePasswordRequest;
import com.trasexp.presentation.dto.ErrorResponse;
import com.trasexp.presentation.dto.UpdateProfileRequest;
import com.trasexp.presentation.dto.UserProfileDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getCurrentUserProfile(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        UserProfileDTO profile = userService.getUserProfile(userId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileDTO> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        UserProfileDTO updated = userService.updateProfile(userId, request);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/me/password")
    public ResponseEntity<?> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication) {
        try {
            Long userId = getUserIdFromAuth(authentication);
            userService.changePassword(userId, request);
            return ResponseEntity.ok().body(new MessageResponse("Password changed successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Password change failed", java.util.List.of(e.getMessage())));
        }
    }

    @PostMapping("/deactivate")
    public ResponseEntity<?> deactivateAccount(
            @Valid @RequestBody AccountActionRequest request,
            Authentication authentication) {
        try {
            Long userId = getUserIdFromAuth(authentication);
            userService.deactivateAccount(userId, request.getPassword());
            return ResponseEntity.ok().body(new MessageResponse("Account deactivated successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Deactivation failed", java.util.List.of(e.getMessage())));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount(
            @Valid @RequestBody AccountActionRequest request,
            Authentication authentication) {
        try {
            Long userId = getUserIdFromAuth(authentication);
            userService.deleteAccount(userId, request.getPassword());
            return ResponseEntity.ok().body(new MessageResponse("Account deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Deletion failed", java.util.List.of(e.getMessage())));
        }
    }

    @PostMapping("/me/avatar")
    public ResponseEntity<?> uploadAvatar(Authentication authentication) {
        // Stub for avatar upload
        return ResponseEntity.ok().body(new MessageResponse("Avatar uploaded successfully (Stub)"));
    }

    private Long getUserIdFromAuth(Authentication authentication) {
        return authenticationHelper.getUserIdFromAuthentication(authentication);
    }

    // Helper DTOs
    static class MessageResponse {
        private String message;

        public MessageResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}

