package com.trasexp.presentation.dto;

import java.time.LocalDateTime;

public class UserPreferencesDTO {
    private String theme;
    private String language;
    private String defaultCurrency;
    private String defaultCategory;
    private Boolean autoCategorizationToggle;
    private Boolean taxCalculationToggle;
    private LocalDateTime updatedAt;

    public UserPreferencesDTO() {
    }

    public UserPreferencesDTO(String theme, String language, String defaultCurrency,
            String defaultCategory, Boolean autoCategorizationToggle,
            Boolean taxCalculationToggle, LocalDateTime updatedAt) {
        this.theme = theme;
        this.language = language;
        this.defaultCurrency = defaultCurrency;
        this.defaultCategory = defaultCategory;
        this.autoCategorizationToggle = autoCategorizationToggle;
        this.taxCalculationToggle = taxCalculationToggle;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
