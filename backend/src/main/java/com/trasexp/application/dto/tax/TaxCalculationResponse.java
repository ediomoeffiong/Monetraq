package com.trasexp.application.dto.tax;

import java.math.BigDecimal;
import java.util.Map;

public class TaxCalculationResponse {
    private String country;
    private BigDecimal income;
    private BigDecimal expenses;
    private BigDecimal taxableIncome;
    private BigDecimal annualTax;
    private BigDecimal monthlyTax;
    private Double effectiveRate;
    private Map<String, Object> breakdown; // Tax bracket details

    public TaxCalculationResponse() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public BigDecimal getExpenses() {
        return expenses;
    }

    public void setExpenses(BigDecimal expenses) {
        this.expenses = expenses;
    }

    public BigDecimal getTaxableIncome() {
        return taxableIncome;
    }

    public void setTaxableIncome(BigDecimal taxableIncome) {
        this.taxableIncome = taxableIncome;
    }

    public BigDecimal getAnnualTax() {
        return annualTax;
    }

    public void setAnnualTax(BigDecimal annualTax) {
        this.annualTax = annualTax;
    }

    public BigDecimal getMonthlyTax() {
        return monthlyTax;
    }

    public void setMonthlyTax(BigDecimal monthlyTax) {
        this.monthlyTax = monthlyTax;
    }

    public Double getEffectiveRate() {
        return effectiveRate;
    }

    public void setEffectiveRate(Double effectiveRate) {
        this.effectiveRate = effectiveRate;
    }

    public Map<String, Object> getBreakdown() {
        return breakdown;
    }

    public void setBreakdown(Map<String, Object> breakdown) {
        this.breakdown = breakdown;
    }
}
