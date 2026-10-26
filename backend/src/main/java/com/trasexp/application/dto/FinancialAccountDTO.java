package com.trasexp.application.dto;

import com.trasexp.domain.AccountType;
import java.math.BigDecimal;

public class FinancialAccountDTO {
    private Long id;
    private String name;
    private AccountType type;
    private String description;
    private BigDecimal balance;
    private Boolean isDefault;
    private Boolean pinRequired;
    private String currency;
    private Boolean includeInOverall;

    public FinancialAccountDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getPinRequired() {
        return pinRequired;
    }

    public void setPinRequired(Boolean pinRequired) {
        this.pinRequired = pinRequired;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getIncludeInOverall() {
        return includeInOverall;
    }

    public void setIncludeInOverall(Boolean includeInOverall) {
        this.includeInOverall = includeInOverall;
    }
}
