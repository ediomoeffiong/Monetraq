package com.trasexp.application.dto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class MonthlySummaryDTO {
    private int year;
    private int month;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal net;
    private Map<String, CategorySummaryDTO> categories = new HashMap<>();

    public MonthlySummaryDTO() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public Map<String, CategorySummaryDTO> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, CategorySummaryDTO> categories) {
        this.categories = categories;
    }
}
