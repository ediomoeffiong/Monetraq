package com.trasexp.application.service.tax;

import com.trasexp.application.dto.tax.TaxCalculationResponse;
import com.trasexp.application.dto.tax.TransactionAggregateDTO;
import com.trasexp.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
public class TaxCalculationService {

    private static final Logger log = LoggerFactory.getLogger(TaxCalculationService.class);

    private final TransactionAggregationService aggregationService;
    private final List<TaxStrategy> taxStrategies;

    @Autowired
    public TaxCalculationService(
            TransactionAggregationService aggregationService,
            List<TaxStrategy> taxStrategies) {
        this.aggregationService = aggregationService;
        this.taxStrategies = taxStrategies;
    }

    @PostConstruct
    public void init() {
        log.info("TaxCalculationService initialized with {} strategies", taxStrategies.size());
        taxStrategies.forEach(s -> log.info("  Registered strategy: country={}, type={}, class={}",
                s.getSupportedCountry(), s.getSupportedType(), s.getClass().getSimpleName()));
    }

    public TaxCalculationResponse calculateTax(
            User user,
            String countryCode,
            String taxType,
            Integer year,
            Long accountId,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        // Determine country code (fallback chain: request > user.countryCode > "NG")
        String finalCountry = Optional.ofNullable(countryCode)
                .orElse(Optional.ofNullable(user.getCountryCode())
                        .orElse("NG"));

        // Determine date range
        LocalDateTime rangeStart;
        LocalDateTime rangeEnd;

        if (startDate != null && endDate != null) {
            rangeStart = startDate;
            rangeEnd = endDate;
        } else {
            int targetYear = Optional.ofNullable(year).orElse(Year.now().getValue());
            rangeStart = LocalDateTime.of(targetYear, 1, 1, 0, 0);
            rangeEnd = LocalDateTime.of(targetYear, 12, 31, 23, 59, 59);
        }

        // Aggregate transactions
        TransactionAggregateDTO aggregateData = aggregationService.aggregateTransactions(
                user, accountId, rangeStart, rangeEnd);

        // Find appropriate strategy (case-insensitive matching)
        TaxStrategy strategy = findStrategy(finalCountry, taxType);
        if (strategy == null) {
            log.error("No tax strategy found for country={}, type={}. Available strategies: {}",
                    finalCountry, taxType, taxStrategies.size());
            throw new IllegalArgumentException(
                    "No tax strategy found for country: " + finalCountry + ", type: " + taxType);
        }

        return strategy.calculate(aggregateData, user);
    }

    private TaxStrategy findStrategy(String country, String type) {
        return taxStrategies.stream()
                .filter(s -> s.getSupportedCountry().equalsIgnoreCase(country)
                        && s.getSupportedType().equalsIgnoreCase(type))
                .findFirst()
                .orElse(null);
    }
}
