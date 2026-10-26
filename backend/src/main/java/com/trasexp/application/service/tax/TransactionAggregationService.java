package com.trasexp.application.service.tax;

import com.trasexp.application.dto.tax.TransactionAggregateDTO;
import com.trasexp.domain.Transaction;
import com.trasexp.domain.FinancialAccount;
import com.trasexp.domain.TransactionType;
import com.trasexp.domain.Category;
import com.trasexp.domain.User;
import com.trasexp.infrastructure.FinancialAccountRepository;
import com.trasexp.infrastructure.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionAggregationService {

    private final TransactionRepository transactionRepository;
    private final FinancialAccountRepository accountRepository;

    public TransactionAggregationService(TransactionRepository transactionRepository,
            FinancialAccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    /**
     * Aggregate transactions for a user within a date range
     */

    public TransactionAggregateDTO aggregateTransactions(User user, Long accountId, LocalDateTime startDate,
            LocalDateTime endDate) {
        List<FinancialAccount> accounts;
        if (accountId != null) {
            accounts = List.of(accountRepository.findByIdAndUser(accountId, user)
                    .orElseThrow(() -> new RuntimeException("Account not found")));
        } else {
            accounts = accountRepository.findByUser(user).stream()
                    .filter(FinancialAccount::getIncludeInOverall)
                    .collect(java.util.stream.Collectors.toList());
        }

        // Sum total income
        BigDecimal totalIncome = transactionRepository.sumByAccountsAndTypeAndDateRange(
                accounts, TransactionType.INCOME, startDate, endDate);

        // Sum total expenses
        BigDecimal totalExpenses = transactionRepository.sumByAccountsAndTypeAndDateRange(
                accounts, TransactionType.EXPENSE, startDate, endDate);

        // Get all transactions in range for category breakdown
        List<Transaction> transactions = transactionRepository.findByAccountsAndDateRange(accounts, startDate, endDate);

        // Group by category
        Map<Category, BigDecimal> incomeByCategory = new HashMap<>();
        Map<Category, BigDecimal> expenseByCategory = new HashMap<>();

        for (Transaction t : transactions) {
            if (t.getType() == TransactionType.INCOME) {
                incomeByCategory.merge(t.getCategory(), t.getAmount(), BigDecimal::add);
            } else {
                expenseByCategory.merge(t.getCategory(), t.getAmount(), BigDecimal::add);
            }
        }

        // Build DTO
        TransactionAggregateDTO dto = new TransactionAggregateDTO();
        dto.setTotalIncome(totalIncome != null ? totalIncome : BigDecimal.ZERO);
        dto.setTotalExpenses(totalExpenses != null ? totalExpenses : BigDecimal.ZERO);
        dto.setIncomeByCategory(incomeByCategory);
        dto.setExpenseByCategory(expenseByCategory);

        return dto;
    }
}
