package com.trasexp.presentation.dto;

import java.time.LocalDateTime;

public class NotificationPreferencesDTO {
    private Boolean emailNotifications;
    private Boolean inAppNotifications;
    private Boolean transactionAlerts;
    private Boolean securityAlerts;
    private Boolean pushNotifications;
    private Boolean smsNotifications;
    private Boolean marketingEmails;
    private Boolean weeklySummary;
    private Boolean taxReportNotifications;
    private LocalDateTime updatedAt;

    public NotificationPreferencesDTO() {
    }

    public NotificationPreferencesDTO(Boolean emailNotifications, Boolean inAppNotifications,
            Boolean transactionAlerts, Boolean securityAlerts,
            Boolean pushNotifications, Boolean smsNotifications,
            Boolean marketingEmails, Boolean weeklySummary,
            Boolean taxReportNotifications, LocalDateTime updatedAt) {
        this.emailNotifications = emailNotifications;
        this.inAppNotifications = inAppNotifications;
        this.transactionAlerts = transactionAlerts;
        this.securityAlerts = securityAlerts;
        this.pushNotifications = pushNotifications;
        this.smsNotifications = smsNotifications;
        this.marketingEmails = marketingEmails;
        this.weeklySummary = weeklySummary;
        this.taxReportNotifications = taxReportNotifications;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Boolean getEmailNotifications() {
        return emailNotifications;
    }

    public void setEmailNotifications(Boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public Boolean getInAppNotifications() {
        return inAppNotifications;
    }

    public void setInAppNotifications(Boolean inAppNotifications) {
        this.inAppNotifications = inAppNotifications;
    }

    public Boolean getTransactionAlerts() {
        return transactionAlerts;
    }

    public void setTransactionAlerts(Boolean transactionAlerts) {
        this.transactionAlerts = transactionAlerts;
    }

    public Boolean getSecurityAlerts() {
        return securityAlerts;
    }

    public void setSecurityAlerts(Boolean securityAlerts) {
        this.securityAlerts = securityAlerts;
    }

    public Boolean getPushNotifications() {
        return pushNotifications;
    }

    public void setPushNotifications(Boolean pushNotifications) {
        this.pushNotifications = pushNotifications;
    }

    public Boolean getSmsNotifications() {
        return smsNotifications;
    }

    public void setSmsNotifications(Boolean smsNotifications) {
        this.smsNotifications = smsNotifications;
    }

    public Boolean getMarketingEmails() {
        return marketingEmails;
    }

    public void setMarketingEmails(Boolean marketingEmails) {
        this.marketingEmails = marketingEmails;
    }

    public Boolean getWeeklySummary() {
        return weeklySummary;
    }

    public void setWeeklySummary(Boolean weeklySummary) {
        this.weeklySummary = weeklySummary;
    }

    public Boolean getTaxReportNotifications() {
        return taxReportNotifications;
    }

    public void setTaxReportNotifications(Boolean taxReportNotifications) {
        this.taxReportNotifications = taxReportNotifications;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
