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
 * Nigeria Value Added Tax (VAT) Strategy
 * Implements 7.5% VAT calculation
 */
@Component
public class NigeriaVATStrategy implements TaxStrategy {

    private static final BigDecimal VAT_RATE = new BigDecimal("0.075"); // 7.5%

    @Override
    public TaxCalculationResponse calculate(TransactionAggregateDTO data, User user) {
        // For VAT, we consider income as taxable revenue
        BigDecimal taxableRevenue = data.getTotalIncome() != null ? data.getTotalIncome() : BigDecimal.ZERO;

        // Calculate VAT
        BigDecimal vatAmount = taxableRevenue.multiply(VAT_RATE).setScale(2, RoundingMode.HALF_UP);

        // Monthly VAT
        BigDecimal monthlyVat = vatAmount.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);

        // Effective rate (should be 7.5%)
        Double effectiveRate = taxableRevenue.compareTo(BigDecimal.ZERO) > 0
                ? VAT_RATE.multiply(new BigDecimal("100")).doubleValue()
                : 0.0;

        // Build response
        TaxCalculationResponse response = new TaxCalculationResponse();
        response.setCountry("NG");
        response.setIncome(taxableRevenue);
        response.setExpenses(BigDecimal.ZERO); // Not relevant for VAT
        response.setTaxableIncome(taxableRevenue);
        response.setAnnualTax(vatAmount);
        response.setMonthlyTax(monthlyVat);
        response.setEffectiveRate(effectiveRate);

        // Add breakdown
        Map<String, Object> breakdown = new LinkedHashMap<>();
        breakdown.put("taxableRevenue", taxableRevenue);
        breakdown.put("vatRate", "7.5%");
        breakdown.put("vatAmount", vatAmount);
        response.setBreakdown(breakdown);

        return response;
    }

    @Override
    public String getSupportedType() {
        return "VAT";
    }

    @Override
    public String getSupportedCountry() {
        return "NG";
    }
}
