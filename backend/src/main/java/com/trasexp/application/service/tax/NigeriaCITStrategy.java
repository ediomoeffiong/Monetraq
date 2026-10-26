package com.trasexp.application.service.tax;

import com.trasexp.application.dto.tax.TaxCalculationResponse;
import com.trasexp.application.dto.tax.TransactionAggregateDTO;
import com.trasexp.domain.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Nigeria Company Income Tax (CIT) Strategy
 * Implements 2026 Nigeria Tax Act for corporate taxation
 */
@Component
public class NigeriaCITStrategy implements TaxStrategy {

    private static final BigDecimal SMALL_COMPANY_TURNOVER_LIMIT = new BigDecimal("100000000"); // 100m
    // Note: We don't have fixed assets in current model, so we'll only check
    // turnover
    private static final BigDecimal CIT_RATE = new BigDecimal("0.30"); // 30%
    private static final BigDecimal DEVELOPMENT_LEVY = new BigDecimal("0.04"); // 4%

    @Override
    public TaxCalculationResponse calculate(TransactionAggregateDTO data, User user) {
        BigDecimal revenue = data.getTotalIncome() != null ? data.getTotalIncome() : BigDecimal.ZERO;
        BigDecimal expenses = data.getTotalExpenses() != null ? data.getTotalExpenses() : BigDecimal.ZERO;

        // Calculate profit
        BigDecimal profit = revenue.subtract(expenses);
        if (profit.compareTo(BigDecimal.ZERO) < 0) {
            profit = BigDecimal.ZERO;
        }

        BigDecimal annualTax;
        boolean isSmallCompany = revenue.compareTo(SMALL_COMPANY_TURNOVER_LIMIT) <= 0;

        if (isSmallCompany) {
            // Small company exemption: 0% CIT
            annualTax = BigDecimal.ZERO;
        } else {
            // Standard CIT: 30% + 4% development levy
            BigDecimal cit = profit.multiply(CIT_RATE);
            BigDecimal levy = profit.multiply(DEVELOPMENT_LEVY);
            annualTax = cit.add(levy);
        }

        // Monthly tax (though CIT is typically annual)
        BigDecimal monthlyTax = annualTax.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);

        // Effective rate
        Double effectiveRate = revenue.compareTo(BigDecimal.ZERO) > 0
                ? annualTax.divide(revenue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).doubleValue()
                : 0.0;

        // Build response
        TaxCalculationResponse response = new TaxCalculationResponse();
        response.setCountry("NG");
        response.setIncome(revenue);
        response.setExpenses(expenses);
        response.setTaxableIncome(profit);
        response.setAnnualTax(annualTax);
        response.setMonthlyTax(monthlyTax);
        response.setEffectiveRate(effectiveRate);

        // Add breakdown
        Map<String, Object> breakdown = new LinkedHashMap<>();
        breakdown.put("revenue", revenue);
        breakdown.put("deductibleExpenses", expenses);
        breakdown.put("taxableProfit", profit);
        breakdown.put("isSmallCompany", isSmallCompany);
        if (!isSmallCompany) {
            breakdown.put("citAmount", profit.multiply(CIT_RATE).setScale(2, RoundingMode.HALF_UP));
            breakdown.put("developmentLevy", profit.multiply(DEVELOPMENT_LEVY).setScale(2, RoundingMode.HALF_UP));
        }
        response.setBreakdown(breakdown);

        return response;
    }

    @Override
    public String getSupportedType() {
        return "CIT";
    }

    @Override
    public String getSupportedCountry() {
        return "NG";
    }
}
