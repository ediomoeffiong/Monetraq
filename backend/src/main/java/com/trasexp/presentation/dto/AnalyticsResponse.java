package com.trasexp.presentation.dto;

import com.trasexp.application.dto.CategorySummaryDTO;
import com.trasexp.application.dto.IncomeSourceSummaryDTO;
import com.trasexp.application.dto.MonthlyTrendDTO;

import java.util.List;

public class AnalyticsResponse {
    private List<CategorySummaryDTO> categoryBreakdown;
    private List<MonthlyTrendDTO> monthlyTrends;
    private List<IncomeSourceSummaryDTO> incomeSourceBreakdown;

    public AnalyticsResponse() {
    }

    public List<CategorySummaryDTO> getCategoryBreakdown() {
        return categoryBreakdown;
    }

    public void setCategoryBreakdown(List<CategorySummaryDTO> categoryBreakdown) {
        this.categoryBreakdown = categoryBreakdown;
    }

    public List<MonthlyTrendDTO> getMonthlyTrends() {
        return monthlyTrends;
    }

    public void setMonthlyTrends(List<MonthlyTrendDTO> monthlyTrends) {
        this.monthlyTrends = monthlyTrends;
    }

    public List<IncomeSourceSummaryDTO> getIncomeSourceBreakdown() {
        return incomeSourceBreakdown;
    }

    public void setIncomeSourceBreakdown(List<IncomeSourceSummaryDTO> incomeSourceBreakdown) {
        this.incomeSourceBreakdown = incomeSourceBreakdown;
    }
}
