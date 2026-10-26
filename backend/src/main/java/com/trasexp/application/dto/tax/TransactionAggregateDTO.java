package com.trasexp.application.dto.tax;

import com.trasexp.domain.Category;
import java.math.BigDecimal;
import java.util.Map;

public class TransactionAggregateDTO {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private Map<Category, BigDecimal> incomeByCategory;
    private Map<Category, BigDecimal> expenseByCategory;

    public TransactionAggregateDTO() {
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public Map<Category, BigDecimal> getIncomeByCategory() {
        return incomeByCategory;
    }

    public void setIncomeByCategory(Map<Category, BigDecimal> incomeByCategory) {
        this.incomeByCategory = incomeByCategory;
    }

    public Map<Category, BigDecimal> getExpenseByCategory() {
        return expenseByCategory;
    }

    public void setExpenseByCategory(Map<Category, BigDecimal> expenseByCategory) {
        this.expenseByCategory = expenseByCategory;
    }
}
