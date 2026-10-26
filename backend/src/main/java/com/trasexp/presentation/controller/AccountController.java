package com.trasexp.presentation.controller;

import com.trasexp.application.dto.FinancialAccountCreateDTO;
import com.trasexp.application.dto.FinancialAccountDTO;
import com.trasexp.application.service.AccountService;
import com.trasexp.domain.User;
import com.trasexp.infrastructure.UserRepository;
import com.trasexp.presentation.dto.AccountActionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<FinancialAccountDTO>> getUserAccounts(@RequestParam("userId") Long userId) {
        User user = getUser(userId);
        return ResponseEntity.ok(accountService.getUserAccounts(user));
    }

    @PostMapping
    public ResponseEntity<FinancialAccountDTO> createAccount(@RequestBody FinancialAccountCreateDTO dto,
            @RequestParam("userId") Long userId) {
        User user = getUser(userId);
        return ResponseEntity.ok(accountService.createAccount(dto, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinancialAccountDTO> updateAccount(@PathVariable("id") Long id,
            @RequestBody FinancialAccountCreateDTO dto,
            @RequestParam("userId") Long userId) {
        User user = getUser(userId);
        return ResponseEntity.ok(accountService.updateAccount(id, dto, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("id") Long id, @RequestParam("userId") Long userId) {
        User user = getUser(userId);
        accountService.deleteAccount(id, user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/verify-pin")
    public ResponseEntity<Boolean> verifyPin(@PathVariable("id") Long id, @RequestBody AccountActionRequest request,
            @RequestParam("userId") Long userId) {
        User user = getUser(userId);
        return ResponseEntity.ok(accountService.verifyPin(id, request.getPin(), user));
    }

    @SuppressWarnings("null")
    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
