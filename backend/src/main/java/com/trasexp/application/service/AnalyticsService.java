package com.trasexp.application.service;

import com.trasexp.application.dto.CategorySummaryDTO;
import com.trasexp.application.dto.IncomeSourceSummaryDTO;
import com.trasexp.application.dto.MonthlyTrendDTO;
import com.trasexp.domain.User;

import java.util.List;

public interface AnalyticsService {
    List<CategorySummaryDTO> getCategoryBreakdown(User user, Long accountId);

    List<MonthlyTrendDTO> getMonthlyTrends(User user, Long accountId, int months);

    List<IncomeSourceSummaryDTO> getIncomeSourceBreakdown(User user, Long accountId);
}
