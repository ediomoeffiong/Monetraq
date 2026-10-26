package com.trasexp.presentation.controller;

import com.trasexp.application.service.AnalyticsService;
import com.trasexp.domain.User;
import com.trasexp.infrastructure.UserRepository;
import com.trasexp.presentation.dto.AnalyticsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@SuppressWarnings("null")
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final UserRepository userRepository;

    @GetMapping("/summary")
    public ResponseEntity<AnalyticsResponse> getAnalyticsSummary(
            @RequestParam("userId") Long userId,
            @RequestParam(value = "accountId", required = false) Long accountId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AnalyticsResponse response = new AnalyticsResponse();
        response.setCategoryBreakdown(analyticsService.getCategoryBreakdown(user, accountId));
        response.setMonthlyTrends(analyticsService.getMonthlyTrends(user, accountId, 6));
        response.setIncomeSourceBreakdown(analyticsService.getIncomeSourceBreakdown(user, accountId));

        return ResponseEntity.ok(response);
    }
}
