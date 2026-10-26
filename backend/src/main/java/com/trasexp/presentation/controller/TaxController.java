package com.trasexp.presentation.controller;

import com.trasexp.application.dto.tax.TaxCalculationResponse;
import com.trasexp.application.service.tax.TaxCalculationService;
import com.trasexp.domain.User;
import com.trasexp.infrastructure.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/analytics/tax")
@SuppressWarnings("null")
public class TaxController {

    private final TaxCalculationService taxCalculationService;
    private final UserRepository userRepository;

    public TaxController(TaxCalculationService taxCalculationService, UserRepository userRepository) {
        this.taxCalculationService = taxCalculationService;
        this.userRepository = userRepository;
    }

    /**
     * Calculate tax automatically from user's transactions
     * 
     * GET /api/analytics/tax/auto?year=2026&type=PIT
     * GET
     * /api/analytics/tax/auto?type=CIT&startDate=2026-01-01T00:00:00&endDate=2026-12-31T23:59:59
     */
    @GetMapping("/auto")
    public ResponseEntity<TaxCalculationResponse> calculateTaxAuto(
            @RequestParam("type") String type,
            @RequestParam("userId") Long userId,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "accountId", required = false) Long accountId,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        // Get user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Calculate tax
        TaxCalculationResponse response = taxCalculationService.calculateTax(
                user,
                country,
                type.toUpperCase(),
                year,
                accountId,
                startDate,
                endDate);

        return ResponseEntity.ok(response);
    }
}
