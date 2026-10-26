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
 * Nigeria Personal Income Tax (PIT) Strategy
 * Implements 2026 Nigeria Tax Act (NTA) progressive tax bands
 */
@Component
public class NigeriaPITStrategy implements TaxStrategy {

    private static final BigDecimal MINIMUM_WAGE_THRESHOLD = new BigDecimal("70000"); // Monthly
    private static final int MONTHS_IN_YEAR = 12;

    // Progressive tax bands (2026 NTA)
    private static final Map<BigDecimal, BigDecimal> TAX_BANDS = new LinkedHashMap<>();
    static {
        TAX_BANDS.put(new BigDecimal("800000"), BigDecimal.ZERO); // 0 – 800,000: 0%
        TAX_BANDS.put(new BigDecimal("3000000"), new BigDecimal("0.15")); // 800,001 – 3,000,000: 15%
        TAX_BANDS.put(new BigDecimal("12000000"), new BigDecimal("0.18")); // 3,000,001 – 12,000,000: 18%
        TAX_BANDS.put(new BigDecimal("25000000"), new BigDecimal("0.21")); // 12,000,001 – 25,000,000: 21%
        TAX_BANDS.put(new BigDecimal("50000000"), new BigDecimal("0.23")); // 25,000,001 – 50,000,000: 23%
        // Above 50,000,000: 25%
    }
    private static final BigDecimal TOP_RATE = new BigDecimal("0.25");

    @Override
    public TaxCalculationResponse calculate(TransactionAggregateDTO data, User user) {
        BigDecimal grossIncome = data.getTotalIncome() != null ? data.getTotalIncome() : BigDecimal.ZERO;

        // For now, we'll use total expenses as deductions (simplified)
        // In a full implementation, we'd split out specific deductible categories
        BigDecimal deductions = data.getTotalExpenses() != null ? data.getTotalExpenses() : BigDecimal.ZERO;

        // Calculate taxable income
        BigDecimal taxableIncome = grossIncome.subtract(deductions);
        if (taxableIncome.compareTo(BigDecimal.ZERO) < 0) {
            taxableIncome = BigDecimal.ZERO;
        }

        // Check minimum wage rule
        BigDecimal averageMonthlyIncome = grossIncome.divide(new BigDecimal(MONTHS_IN_YEAR), 2, RoundingMode.HALF_UP);
        BigDecimal annualTax;

        if (averageMonthlyIncome.compareTo(MINIMUM_WAGE_THRESHOLD) <= 0) {
            annualTax = BigDecimal.ZERO;
        } else {
            annualTax = calculateProgressiveTax(taxableIncome);
        }

        // Calculate monthly tax
        BigDecimal monthlyTax = annualTax.divide(new BigDecimal(MONTHS_IN_YEAR), 2, RoundingMode.HALF_UP);

        // Calculate effective rate
        Double effectiveRate = grossIncome.compareTo(BigDecimal.ZERO) > 0
                ? annualTax.divide(grossIncome, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).doubleValue()
                : 0.0;

        // Build response
        TaxCalculationResponse response = new TaxCalculationResponse();
        response.setCountry("NG");
        response.setIncome(grossIncome);
        response.setExpenses(deductions);
        response.setTaxableIncome(taxableIncome);
        response.setAnnualTax(annualTax);
        response.setMonthlyTax(monthlyTax);
        response.setEffectiveRate(effectiveRate);

        // Add breakdown
        Map<String, Object> breakdown = new LinkedHashMap<>();
        breakdown.put("averageMonthlyIncome", averageMonthlyIncome);
        breakdown.put("minimumWageExempt", averageMonthlyIncome.compareTo(MINIMUM_WAGE_THRESHOLD) <= 0);
        response.setBreakdown(breakdown);

        return response;
    }

    private BigDecimal calculateProgressiveTax(BigDecimal taxableIncome) {
        BigDecimal tax = BigDecimal.ZERO;
        BigDecimal previousBand = BigDecimal.ZERO;

        for (Map.Entry<BigDecimal, BigDecimal> entry : TAX_BANDS.entrySet()) {
            BigDecimal bandLimit = entry.getKey();
            BigDecimal rate = entry.getValue();

            if (taxableIncome.compareTo(bandLimit) <= 0) {
                // Income falls within this band
                BigDecimal taxableInBand = taxableIncome.subtract(previousBand);
                tax = tax.add(taxableInBand.multiply(rate));
                return tax.setScale(2, RoundingMode.HALF_UP);
            } else {
                // Income exceeds this band, tax the full band
                BigDecimal taxableInBand = bandLimit.subtract(previousBand);
                tax = tax.add(taxableInBand.multiply(rate));
                previousBand = bandLimit;
            }
        }

        // Income exceeds all bands, apply top rate to remainder
        BigDecimal remainingIncome = taxableIncome.subtract(previousBand);
        tax = tax.add(remainingIncome.multiply(TOP_RATE));

        return tax.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String getSupportedType() {
        return "PIT";
    }

    @Override
    public String getSupportedCountry() {
        return "NG";
    }
}
