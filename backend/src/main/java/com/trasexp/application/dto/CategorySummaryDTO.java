package com.trasexp.application.dto;

import com.trasexp.domain.Category;

import java.math.BigDecimal;

public class CategorySummaryDTO {
    private Category category;
    private BigDecimal amount;
    private double percentage;
    private BigDecimal income = BigDecimal.ZERO;
    private BigDecimal expense = BigDecimal.ZERO;

    public CategorySummaryDTO() {
    }

    public CategorySummaryDTO(Category category, BigDecimal amount, double percentage, BigDecimal income,
            BigDecimal expense) {
        this.category = category;
        this.amount = amount;
        this.percentage = percentage;
        this.income = income;
        this.expense = expense;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }
}
