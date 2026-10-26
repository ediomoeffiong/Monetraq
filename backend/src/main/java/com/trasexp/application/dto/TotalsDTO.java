package com.trasexp.application.dto;

import java.math.BigDecimal;

public class TotalsDTO {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;

    public TotalsDTO() {
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
}
