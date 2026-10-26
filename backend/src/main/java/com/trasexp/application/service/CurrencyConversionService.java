package com.trasexp.application.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyConversionService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<String, BigDecimal> ratesCache = new ConcurrentHashMap<>();
    private long lastCacheUpdate = 0;
    private static final long CACHE_DURATION_MS = 3600000; // 1 hour

    public BigDecimal convert(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (amount == null) {
            return BigDecimal.ZERO;
        }
        if (fromCurrency == null || toCurrency == null || fromCurrency.equalsIgnoreCase(toCurrency)) {
            return amount;
        }

        BigDecimal rate = getExchangeRate(fromCurrency, toCurrency);
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        updateCacheIfNeeded();

        // Base is USD in the API I found
        BigDecimal fromRate = ratesCache.getOrDefault(fromCurrency.toUpperCase(), BigDecimal.ONE);
        BigDecimal toRate = ratesCache.getOrDefault(toCurrency.toUpperCase(), BigDecimal.ONE);

        if (fromRate.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ONE;
        }

        // rate = toRate / fromRate
        return toRate.divide(fromRate, 6, RoundingMode.HALF_UP);
    }

    private synchronized void updateCacheIfNeeded() {
        long now = System.currentTimeMillis();
        if (now - lastCacheUpdate < CACHE_DURATION_MS && !ratesCache.isEmpty()) {
            return;
        }

        try {
            String url = "https://open.er-api.com/v6/latest/USD";
            Map<?, ?> response = restTemplate.getForObject(url, Map.class);
            if (response != null && "success".equals(response.get("result"))) {
                Object ratesObj = response.get("rates");
                if (ratesObj instanceof Map<?, ?> rates) {
                    rates.forEach((code, rate) -> {
                        if (code != null && rate != null) {
                            ratesCache.put(code.toString(), new BigDecimal(rate.toString()));
                        }
                    });
                    lastCacheUpdate = now;
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching exchange rates: " + e.getMessage());
            // Fallback rates if API fails
            if (ratesCache.isEmpty()) {
                ratesCache.put("USD", BigDecimal.ONE);
                ratesCache.put("NGN", new BigDecimal("1500")); // Simple fallback
                ratesCache.put("EUR", new BigDecimal("0.92"));
                ratesCache.put("GBP", new BigDecimal("0.79"));
            }
        }
    }
}
