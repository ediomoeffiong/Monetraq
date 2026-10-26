package com.trasexp.application.service.tax;

import com.trasexp.application.dto.tax.TaxCalculationResponse;
import com.trasexp.application.dto.tax.TransactionAggregateDTO;
import com.trasexp.domain.User;

public interface TaxStrategy {
    /**
     * Calculate tax based on aggregated transaction data
     */
    TaxCalculationResponse calculate(TransactionAggregateDTO data, User user);

    /**
     * Get the tax type supported by this strategy (PIT, CIT, VAT)
     */
    String getSupportedType();

    /**
     * Get the country code supported by this strategy (NG, GH, etc.)
     */
    String getSupportedCountry();
}
