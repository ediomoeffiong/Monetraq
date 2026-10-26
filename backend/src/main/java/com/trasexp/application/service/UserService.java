package com.trasexp.application.service;

import com.trasexp.domain.AccountStatus;
import com.trasexp.domain.User;
import com.trasexp.infrastructure.*;
import com.trasexp.presentation.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSettingsRepository userSettingsRepository;
    private final NotificationPreferencesRepository notificationPreferencesRepository;
    private final SecuritySessionRepository securitySessionRepository;
    private final LoginHistoryRepository loginHistoryRepository;

    public UserProfileDTO getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserProfileDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getPhoneNumber(),
                user.getProfilePhotoUrl(),
                user.getTwoFactorEnabled(),
                user.getAccountStatus(),
                user.getKycStatus(),
                user.getVerificationStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }

    @Transactional
    public UserProfileDTO updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if email is being changed and if it's already taken
        if (!user.getEmail().equals(request.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already in use");
            }
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setProfilePhotoUrl(request.getProfilePhotoUrl());

        User updatedUser = userRepository.save(user);

        return new UserProfileDTO(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getEmail(),
                updatedUser.getFullName(),
                updatedUser.getPhoneNumber(),
                updatedUser.getProfilePhotoUrl(),
                updatedUser.getTwoFactorEnabled(),
                updatedUser.getAccountStatus(),
                updatedUser.getKycStatus(),
                updatedUser.getVerificationStatus(),
                updatedUser.getCreatedAt(),
                updatedUser.getUpdatedAt());
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // Verify new password and confirmation match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("New password and confirmation do not match");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setLastPasswordChange(LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void deactivateAccount(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Password is incorrect");
        }

        user.setAccountStatus(AccountStatus.DEACTIVATED);
        userRepository.save(user);
    }

    @Transactional
    public void deleteAccount(Long userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Verify password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Password is incorrect");
        }

        // Soft delete - mark as deleted
        user.setAccountStatus(AccountStatus.DELETED);
        userRepository.save(user);

        // Clean up related data
        userSettingsRepository.deleteByUserId(userId);
        notificationPreferencesRepository.deleteByUserId(userId);
        securitySessionRepository.deleteByUserId(userId);
        loginHistoryRepository.deleteByUserId(userId);
    }
}
