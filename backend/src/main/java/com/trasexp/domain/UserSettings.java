package com.trasexp.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_settings")
@EntityListeners(AuditingEntityListener.class)
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Theme theme = Theme.SYSTEM;

    @Column(nullable = false)
    private String language = "en";

    @Column(nullable = false)
    private String defaultCurrency = "NGN";

    private String defaultCategory;

    @Column(nullable = false)
    private Boolean autoCategorizationToggle = true;

    @Column(nullable = false)
    private Boolean taxCalculationToggle = false;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public UserSettings() {
    }

    public UserSettings(Long userId) {
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

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(String defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDefaultCategory() {
        return defaultCategory;
    }

    public void setDefaultCategory(String defaultCategory) {
        this.defaultCategory = defaultCategory;
    }

    public Boolean getAutoCategorizationToggle() {
        return autoCategorizationToggle;
    }

    public void setAutoCategorizationToggle(Boolean autoCategorizationToggle) {
        this.autoCategorizationToggle = autoCategorizationToggle;
    }

    public Boolean getTaxCalculationToggle() {
        return taxCalculationToggle;
    }

    public void setTaxCalculationToggle(Boolean taxCalculationToggle) {
        this.taxCalculationToggle = taxCalculationToggle;
    }

    public enum Theme {
        LIGHT,
        DARK,
        SYSTEM
    }
}
