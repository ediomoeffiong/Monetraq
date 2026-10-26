package com.trasexp.application.service;

import com.trasexp.domain.User;
import com.trasexp.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationHelper {

    private final UserRepository userRepository;

    public Long getUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            log.warn("Authentication is null or has no name");
            throw new RuntimeException("User not authenticated");
        }

        String email = authentication.getName();
        log.debug("Getting user ID for email: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getId();
    }

    public User getUserFromAuthentication(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            log.warn("Authentication is null or has no name");
            throw new RuntimeException("User not authenticated");
        }

        String email = authentication.getName();
        log.debug("Getting user for email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
