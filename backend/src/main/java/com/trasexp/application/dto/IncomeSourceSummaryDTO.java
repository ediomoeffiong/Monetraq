package com.trasexp.application.dto;

import java.math.BigDecimal;

public class IncomeSourceSummaryDTO {
    private Long id;
    private String title;
    private BigDecimal amount;
    private BigDecimal allocatedAmount;
    private BigDecimal remainingBalance;
    private double percentageAllocated;

    public IncomeSourceSummaryDTO() {
    }

    public IncomeSourceSummaryDTO(Long id, String title, BigDecimal amount, BigDecimal allocatedAmount,
            BigDecimal remainingBalance, double percentageAllocated) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.allocatedAmount = allocatedAmount;
        this.remainingBalance = remainingBalance;
        this.percentageAllocated = percentageAllocated;
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

    public double getPercentageAllocated() {
        return percentageAllocated;
    }

    public void setPercentageAllocated(double percentageAllocated) {
        this.percentageAllocated = percentageAllocated;
    }
}
