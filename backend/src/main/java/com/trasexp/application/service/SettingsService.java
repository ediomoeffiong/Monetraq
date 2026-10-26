package com.trasexp.application.service;

import com.trasexp.domain.NotificationPreferences;
import com.trasexp.domain.UserSettings;
import com.trasexp.infrastructure.NotificationPreferencesRepository;
import com.trasexp.infrastructure.UserSettingsRepository;
import com.trasexp.presentation.dto.NotificationPreferencesDTO;
import com.trasexp.presentation.dto.UserPreferencesDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SettingsService {

    private final UserSettingsRepository userSettingsRepository;
    private final NotificationPreferencesRepository notificationPreferencesRepository;

    public NotificationPreferencesDTO getNotificationPreferences(Long userId) {
        NotificationPreferences prefs = notificationPreferencesRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultNotificationPreferences(userId));

        return new NotificationPreferencesDTO(
                prefs.getEmailNotifications(),
                prefs.getInAppNotifications(),
                prefs.getTransactionAlerts(),
                prefs.getSecurityAlerts(),
                prefs.getPushNotifications(),
                prefs.getSmsNotifications(),
                prefs.getMarketingEmails(),
                prefs.getWeeklySummary(),
                prefs.getTaxReportNotifications(),
                prefs.getUpdatedAt());
    }

    @Transactional
    public NotificationPreferencesDTO updateNotificationPreferences(Long userId, NotificationPreferencesDTO dto) {
        NotificationPreferences prefs = notificationPreferencesRepository.findByUserId(userId)
                .orElseGet(() -> new NotificationPreferences(userId));

        prefs.setEmailNotifications(dto.getEmailNotifications());
        prefs.setInAppNotifications(dto.getInAppNotifications());
        prefs.setTransactionAlerts(dto.getTransactionAlerts());
        prefs.setPushNotifications(dto.getPushNotifications());
        prefs.setSmsNotifications(dto.getSmsNotifications());
        prefs.setMarketingEmails(dto.getMarketingEmails());
        prefs.setWeeklySummary(dto.getWeeklySummary());
        prefs.setTaxReportNotifications(dto.getTaxReportNotifications());

        NotificationPreferences saved = notificationPreferencesRepository.save(prefs);

        return new NotificationPreferencesDTO(
                saved.getEmailNotifications(),
                saved.getInAppNotifications(),
                saved.getTransactionAlerts(),
                saved.getSecurityAlerts(),
                saved.getPushNotifications(),
                saved.getSmsNotifications(),
                saved.getMarketingEmails(),
                saved.getWeeklySummary(),
                saved.getTaxReportNotifications(),
                saved.getUpdatedAt());
    }

    public UserPreferencesDTO getUserPreferences(Long userId) {
        UserSettings settings = userSettingsRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultUserSettings(userId));

        return new UserPreferencesDTO(
                settings.getTheme().name(),
                settings.getLanguage(),
                settings.getDefaultCurrency(),
                settings.getDefaultCategory(),
                settings.getAutoCategorizationToggle(),
                settings.getTaxCalculationToggle(),
                settings.getUpdatedAt());
    }

    @Transactional
    public UserPreferencesDTO updateUserPreferences(Long userId, UserPreferencesDTO dto) {
        UserSettings settings = userSettingsRepository.findByUserId(userId)
                .orElseGet(() -> new UserSettings(userId));

        try {
            settings.setTheme(UserSettings.Theme.valueOf(dto.getTheme().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid theme value. Must be LIGHT, DARK, or SYSTEM");
        }

        settings.setLanguage(dto.getLanguage());
        settings.setDefaultCurrency(dto.getDefaultCurrency());
        settings.setDefaultCategory(dto.getDefaultCategory());
        settings.setAutoCategorizationToggle(dto.getAutoCategorizationToggle());
        settings.setTaxCalculationToggle(dto.getTaxCalculationToggle());

        UserSettings saved = userSettingsRepository.save(settings);

        return new UserPreferencesDTO(
                saved.getTheme().name(),
                saved.getLanguage(),
                saved.getDefaultCurrency(),
                saved.getDefaultCategory(),
                saved.getAutoCategorizationToggle(),
                saved.getTaxCalculationToggle(),
                saved.getUpdatedAt());
    }

    private NotificationPreferences createDefaultNotificationPreferences(Long userId) {
        NotificationPreferences prefs = new NotificationPreferences(userId);
        return notificationPreferencesRepository.save(prefs);
    }

    private UserSettings createDefaultUserSettings(Long userId) {
        UserSettings settings = new UserSettings(userId);
        return userSettingsRepository.save(settings);
    }
}
