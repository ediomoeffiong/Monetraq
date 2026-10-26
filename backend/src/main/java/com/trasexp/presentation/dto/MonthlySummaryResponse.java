package com.trasexp.presentation.dto;

import lombok.Data;

import com.trasexp.application.dto.CategorySummaryDTO;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class MonthlySummaryResponse {
    private int year;
    private int month;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal net;
    private Map<String, CategorySummaryDTO> categories;
}
