package com.trasexp.application.dto;

import com.trasexp.domain.Category;
import com.trasexp.domain.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionDTO {
    private Long id;
    private String title;
    private BigDecimal amount;
    private TransactionType type;
    private Category category;
    private Long accountId;
    private String accountName;
    private LocalDateTime date;

    // Allocation fields
    private BigDecimal allocatedAmount;
    private BigDecimal remainingBalance;
    private Long fundingIncomeId;
    private String fundingIncomeTitle;
    private String currency;

    public TransactionDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public Long getFundingIncomeId() {
        return fundingIncomeId;
    }

    public void setFundingIncomeId(Long fundingIncomeId) {
        this.fundingIncomeId = fundingIncomeId;
    }

    public String getFundingIncomeTitle() {
        return fundingIncomeTitle;
    }

    public void setFundingIncomeTitle(String fundingIncomeTitle) {
        this.fundingIncomeTitle = fundingIncomeTitle;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
