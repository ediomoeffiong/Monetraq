package com.trasexp.application.service;

import com.trasexp.application.dto.CategorySummaryDTO;
import com.trasexp.application.dto.IncomeSourceSummaryDTO;
import com.trasexp.application.dto.MonthlyTrendDTO;
import com.trasexp.domain.*;
import com.trasexp.infrastructure.FinancialAccountRepository;
import com.trasexp.infrastructure.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class AnalyticsServiceImpl implements AnalyticsService {

        private final TransactionRepository transactionRepository;
        private final FinancialAccountRepository accountRepository;

        @Override
        public List<CategorySummaryDTO> getCategoryBreakdown(User user, Long accountId) {
                List<Transaction> transactions;
                if (accountId == null) {
                        List<FinancialAccount> accounts = accountRepository.findByUser(user);
                        transactions = transactionRepository.findByAccountIn(accounts);
                } else {
                        FinancialAccount account = accountRepository.findByIdAndUser(accountId, user)
                                        .orElseThrow(() -> new RuntimeException("Account not found"));
                        transactions = transactionRepository.findByAccount(account);
                }

                List<Transaction> expenses = transactions.stream()
                                .filter(t -> t.getType() == TransactionType.EXPENSE)
                                .collect(Collectors.toList());

                BigDecimal totalExpense = expenses.stream()
                                .map(Transaction::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (totalExpense.compareTo(BigDecimal.ZERO) == 0) {
                        return Collections.emptyList();
                }

                Map<Category, BigDecimal> categoryTotals = expenses.stream()
                                .filter(t -> t.getCategory() != null)
                                .collect(Collectors.groupingBy(
                                                Transaction::getCategory,
                                                Collectors.mapping(Transaction::getAmount,
                                                                Collectors.reducing(BigDecimal.ZERO,
                                                                                BigDecimal::add))));

                return categoryTotals.entrySet().stream()
                                .map(entry -> {
                                        BigDecimal amount = entry.getValue();
                                        double percentage = amount.multiply(new BigDecimal(100))
                                                        .divide(totalExpense, 2, RoundingMode.HALF_UP)
                                                        .doubleValue();
                                        return new CategorySummaryDTO(entry.getKey(), amount, percentage,
                                                        BigDecimal.ZERO, BigDecimal.ZERO);
                                })

                                .sorted(Comparator.comparing(CategorySummaryDTO::getAmount).reversed())
                                .collect(Collectors.toList());
        }

        @Override
        public List<MonthlyTrendDTO> getMonthlyTrends(User user, Long accountId, int months) {
                List<Transaction> transactions;
                if (accountId == null) {
                        List<FinancialAccount> accounts = accountRepository.findByUser(user);
                        transactions = transactionRepository.findByAccountIn(accounts);
                } else {
                        FinancialAccount account = accountRepository.findByIdAndUser(accountId, user)
                                        .orElseThrow(() -> new RuntimeException("Account not found"));
                        transactions = transactionRepository.findByAccount(account);
                }

                LocalDate now = LocalDate.now();
                List<MonthlyTrendDTO> trends = new ArrayList<>();

                for (int i = months - 1; i >= 0; i--) {
                        LocalDate targetDate = now.minusMonths(i);
                        int year = targetDate.getYear();
                        int monthValue = targetDate.getMonthValue();
                        String monthName = targetDate.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

                        BigDecimal income = transactions.stream()
                                        .filter(t -> {
                                                LocalDateTime d = t.getDate();
                                                return d.getYear() == year && d.getMonthValue() == monthValue
                                                                && t.getType() == TransactionType.INCOME;
                                        })
                                        .map(Transaction::getAmount)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                        BigDecimal expense = transactions.stream()
                                        .filter(t -> {
                                                LocalDateTime d = t.getDate();
                                                return d.getYear() == year && d.getMonthValue() == monthValue
                                                                && t.getType() == TransactionType.EXPENSE;
                                        })
                                        .map(Transaction::getAmount)
                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                        trends.add(new MonthlyTrendDTO(monthName, monthValue, year, income, expense));
                }

                return trends;
        }

        @Override
        public List<IncomeSourceSummaryDTO> getIncomeSourceBreakdown(User user, Long accountId) {
                List<Transaction> transactions;
                if (accountId == null) {
                        List<FinancialAccount> accounts = accountRepository.findByUser(user);
                        transactions = transactionRepository.findByAccountIn(accounts);
                } else {
                        FinancialAccount account = accountRepository.findByIdAndUser(accountId, user)
                                        .orElseThrow(() -> new RuntimeException("Account not found"));
                        transactions = transactionRepository.findByAccount(account);
                }

                return transactions.stream()
                                .filter(t -> t.getType() == TransactionType.INCOME)
                                .map(t -> {
                                        double percentage = t.getAmount().compareTo(BigDecimal.ZERO) > 0
                                                        ? t.getAllocatedAmount().multiply(new BigDecimal(100))
                                                                        .divide(t.getAmount(), 2, RoundingMode.HALF_UP)
                                                                        .doubleValue()
                                                        : 0;
                                        return new IncomeSourceSummaryDTO(
                                                        t.getId(),
                                                        t.getTitle(),
                                                        t.getAmount(),
                                                        t.getAllocatedAmount(),
                                                        t.getRemainingBalance(),
                                                        percentage);
                                })
                                .sorted(Comparator.comparing(IncomeSourceSummaryDTO::getRemainingBalance).reversed())
                                .collect(Collectors.toList());
        }
}
