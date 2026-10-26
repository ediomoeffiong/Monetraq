package com.trasexp.application.dto;

import com.trasexp.domain.Category;
import com.trasexp.domain.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionCreateDTO {
    private String title;
    private BigDecimal amount;
    private TransactionType type;
    private Category category;
    private Long accountId;
    private LocalDateTime date;
    private Long fundingIncomeId;
    private String currency;

    public TransactionCreateDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getFundingIncomeId() {
        return fundingIncomeId;
    }

    public void setFundingIncomeId(Long fundingIncomeId) {
        this.fundingIncomeId = fundingIncomeId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
