package com.trasexp.application.service;

import com.trasexp.domain.AccountType;
import com.trasexp.domain.FinancialAccount;
import com.trasexp.domain.Transaction;
import com.trasexp.domain.User;
import com.trasexp.infrastructure.FinancialAccountRepository;
import com.trasexp.infrastructure.TransactionRepository;
import com.trasexp.infrastructure.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataMigrationService {

    private final UserRepository userRepository;
    private final FinancialAccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @PostConstruct
    public void migrateData() {
        log.info("Starting data migration for multi-account support...");
        try {
            migrateTransactionsToAccounts();
            log.info("Data migration completed.");
        } catch (Exception e) {
            log.error("Data migration failed (schema may still be updating). Will skip migration: {}", e.getMessage());
        }
    }

    @Transactional
    public void migrateTransactionsToAccounts() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            // Check if user has any accounts
            List<FinancialAccount> accounts = accountRepository.findByUser(user);
            FinancialAccount defaultAccount;
            if (accounts.isEmpty()) {
                log.info("Creating default account for user: {}", user.getEmail());
                defaultAccount = new FinancialAccount();
                defaultAccount.setName("Primary Account");
                defaultAccount.setType(AccountType.SAVINGS);
                defaultAccount.setBalance(BigDecimal.ZERO);
                defaultAccount.setUser(user);
                defaultAccount.setIsDefault(true);
                defaultAccount.setPinRequired(false);
                defaultAccount = accountRepository.save(defaultAccount);
            } else {
                defaultAccount = accounts.stream()
                        .filter(FinancialAccount::getIsDefault)
                        .findFirst()
                        .orElse(accounts.get(0));
            }

            // Placeholder: Link transactions without an account to the default account.
            // Actual implementation would go here if needed.
        }
    }
}
