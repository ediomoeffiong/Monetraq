package com.trasexp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @NotNull
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private FinancialAccount account;

    @NotNull
    private BigDecimal allocatedAmount = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "funding_income_id")
    private Transaction fundingIncome;

    @CreatedDate
    private LocalDateTime createdAt;

    @NotBlank
    @Column(nullable = false)
    private String currency = "NGN";

    public Transaction() {
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public FinancialAccount getAccount() {
        return account;
    }

    public void setAccount(FinancialAccount account) {
        this.account = account;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getAllocatedAmount() {
        return allocatedAmount;
    }

    public void setAllocatedAmount(BigDecimal allocatedAmount) {
        this.allocatedAmount = allocatedAmount;
    }

    public Transaction getFundingIncome() {
        return fundingIncome;
    }

    public void setFundingIncome(Transaction fundingIncome) {
        this.fundingIncome = fundingIncome;
    }

    public BigDecimal getRemainingBalance() {
        if (type != TransactionType.INCOME)
            return BigDecimal.ZERO;
        return amount.subtract(allocatedAmount);
    }
}
