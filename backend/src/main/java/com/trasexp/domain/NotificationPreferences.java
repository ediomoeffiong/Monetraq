package com.trasexp.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_preferences")
@EntityListeners(AuditingEntityListener.class)
public class NotificationPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private Boolean emailNotifications = true;

    @Column(nullable = false)
    private Boolean inAppNotifications = true;

    @Column(nullable = false)
    private Boolean transactionAlerts = true;

    @Column(nullable = false)
    private Boolean securityAlerts = true; // Always true, read-only

    @Column(nullable = false)
    private Boolean pushNotifications = true;

    @Column(nullable = false)
    private Boolean smsNotifications = false;

    @Column(nullable = false)
    private Boolean marketingEmails = true;

    @Column(nullable = false)
    private Boolean weeklySummary = true;

    @Column(nullable = false)
    private Boolean taxReportNotifications = true;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public NotificationPreferences() {
    }

    public NotificationPreferences(Long userId) {
        this.userId = userId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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
        // Security alerts should always be enabled
        this.securityAlerts = true;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
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
}
