package com.trasexp.presentation.controller;

import com.trasexp.application.service.AuthenticationHelper;
import com.trasexp.application.service.SecurityService;
import com.trasexp.presentation.dto.LoginHistoryDTO;
import com.trasexp.presentation.dto.SecuritySessionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/security")
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;
    private final AuthenticationHelper authenticationHelper;

    @GetMapping("/sessions")
    public ResponseEntity<List<SecuritySessionDTO>> getActiveSessions(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        List<SecuritySessionDTO> sessions = securityService.getActiveSessions(userId);
        return ResponseEntity.ok(sessions);
    }

    @DeleteMapping("/sessions/{sessionId}")
    public ResponseEntity<?> terminateSession(
            @PathVariable("sessionId") Long sessionId,
            Authentication authentication) {
        try {
            Long userId = getUserIdFromAuth(authentication);
            securityService.terminateSession(userId, sessionId);
            return ResponseEntity.ok().body(new MessageResponse("Session terminated successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/login-history")
    public ResponseEntity<List<LoginHistoryDTO>> getLoginHistory(
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        List<LoginHistoryDTO> history = securityService.getLoginHistory(userId, limit);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/2fa/enable")
    public ResponseEntity<?> enable2FA(Authentication authentication) {
        // Stub for enabling 2FA
        return ResponseEntity.ok().body(new MessageResponse("2FA enabled successfully (Stub)"));
    }

    @PostMapping("/2fa/disable")
    public ResponseEntity<?> disable2FA(Authentication authentication) {
        // Stub for disabling 2FA
        return ResponseEntity.ok().body(new MessageResponse("2FA disabled successfully (Stub)"));
    }

    private Long getUserIdFromAuth(Authentication authentication) {
        return authenticationHelper.getUserIdFromAuthentication(authentication);
    }

    // Helper DTO
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
