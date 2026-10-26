package com.trasexp.presentation.controller;

import com.trasexp.application.dto.MonthlySummaryDTO;
import com.trasexp.application.dto.TransactionCreateDTO;
import com.trasexp.application.dto.TransactionDTO;
import com.trasexp.application.dto.TransactionUpdateDTO;
import com.trasexp.application.service.TransactionService;
import com.trasexp.domain.Category;
import com.trasexp.domain.User;
import com.trasexp.infrastructure.UserRepository;
import com.trasexp.presentation.dto.MonthlySummaryResponse;
import com.trasexp.presentation.dto.TransactionRequest;
import com.trasexp.presentation.dto.TransactionResponse;
import com.trasexp.application.dto.TotalsDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@SuppressWarnings("null")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(@RequestParam("userId") Long userId,
            @RequestParam(value = "accountId", required = false) Long accountId) {
        User user = getUser(userId);
        List<TransactionResponse> responses = transactionService.getAllTransactions(user, accountId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable("id") Long id,
            @RequestParam("userId") Long userId) {
        User user = getUser(userId);
        TransactionResponse response = mapToResponse(transactionService.getTransactionById(id, user));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest request,
            @RequestParam("userId") Long userId) {
        User user = getUser(userId);
        TransactionResponse response = mapToResponse(
                transactionService.createTransaction(mapToCreateDTO(request), user, request.getAccountId()));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable("id") Long id,
            @Valid @RequestBody TransactionRequest request, @RequestParam("userId") Long userId) {
        User user = getUser(userId);
        TransactionResponse response = mapToResponse(
                transactionService.updateTransaction(id, mapToUpdateDTO(request), user));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {
        User user = getUser(userId);
        transactionService.deleteTransaction(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/monthly")
    public ResponseEntity<MonthlySummaryResponse> getMonthlySummary(@RequestParam("year") int year,
            @RequestParam("month") int month,
            @RequestParam("userId") Long userId, @RequestParam(value = "accountId", required = false) Long accountId) {
        User user = getUser(userId);
        MonthlySummaryResponse response = mapToMonthlyResponse(
                transactionService.getMonthlySummary(user, accountId, year, month));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByCategory(
            @RequestParam("category") Category category,
            @RequestParam("userId") Long userId, @RequestParam(value = "accountId", required = false) Long accountId) {
        User user = getUser(userId);
        List<TransactionResponse> responses = transactionService.getTransactionsByCategory(user, accountId, category)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/totals")
    public ResponseEntity<TotalsDTO> getTotals(@RequestParam("userId") Long userId,
            @RequestParam(value = "accountId", required = false) Long accountId) {
        User user = getUser(userId);
        return ResponseEntity.ok(transactionService.getTotals(user, accountId));
    }

    @GetMapping("/available-incomes")
    public ResponseEntity<List<TransactionResponse>> getAvailableIncomes(@RequestParam("userId") Long userId,
            @RequestParam(value = "accountId", required = false) Long accountId) {
        User user = getUser(userId);
        List<TransactionResponse> responses = transactionService.getAvailableIncomes(user, accountId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private TransactionResponse mapToResponse(TransactionDTO dto) {
        TransactionResponse response = new TransactionResponse();
        response.setId(dto.getId());
        response.setTitle(dto.getTitle());
        response.setAmount(dto.getAmount());
        response.setType(dto.getType());
        response.setCategory(dto.getCategory());
        response.setDate(dto.getDate());
        response.setAccountId(dto.getAccountId());
        response.setAccountName(dto.getAccountName());
        response.setCurrency(dto.getCurrency());

        // Allocation fields
        response.setAllocatedAmount(dto.getAllocatedAmount());
        response.setRemainingBalance(dto.getRemainingBalance());
        response.setFundingIncomeId(dto.getFundingIncomeId());
        response.setFundingIncomeTitle(dto.getFundingIncomeTitle());

        return response;
    }

    private TransactionCreateDTO mapToCreateDTO(TransactionRequest request) {
        TransactionCreateDTO dto = new TransactionCreateDTO();
        dto.setTitle(request.getTitle());
        dto.setAmount(request.getAmount());
        dto.setType(request.getType());
        dto.setCategory(request.getCategory());
        dto.setDate(request.getDate());
        dto.setFundingIncomeId(request.getFundingIncomeId());
        return dto;
    }

    private TransactionUpdateDTO mapToUpdateDTO(TransactionRequest request) {
        TransactionUpdateDTO dto = new TransactionUpdateDTO();
        dto.setTitle(request.getTitle());
        dto.setAmount(request.getAmount());
        dto.setType(request.getType());
        dto.setCategory(request.getCategory());
        dto.setDate(request.getDate());
        dto.setFundingIncomeId(request.getFundingIncomeId());
        return dto;
    }

    private MonthlySummaryResponse mapToMonthlyResponse(MonthlySummaryDTO dto) {
        MonthlySummaryResponse response = new MonthlySummaryResponse();
        response.setYear(dto.getYear());
        response.setMonth(dto.getMonth());
        response.setTotalIncome(dto.getTotalIncome());
        response.setTotalExpense(dto.getTotalExpense());
        response.setNet(dto.getNet());
        response.setCategories(dto.getCategories());
        return response;
    }
}
