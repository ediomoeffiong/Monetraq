package com.trasexp.application.service;

import com.trasexp.application.dto.*;
import com.trasexp.domain.Transaction;
import com.trasexp.domain.FinancialAccount;
import com.trasexp.domain.TransactionType;
import com.trasexp.domain.Category;
import com.trasexp.domain.User;
import com.trasexp.infrastructure.FinancialAccountRepository;
import com.trasexp.infrastructure.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final FinancialAccountRepository accountRepository;
    private final AccountService accountService;

    @Override
    @Transactional
    public TransactionDTO createTransaction(TransactionCreateDTO dto, User user, Long accountId) {
        validateDate(dto.getDate());
        FinancialAccount account;
        if (accountId != null) {
            account = accountRepository.findByIdAndUser(accountId, user)
                    .orElseThrow(() -> new RuntimeException("Account not found"));
        } else {
            account = accountRepository.findByUserAndIsDefaultTrue(user)
                    .orElseThrow(() -> new RuntimeException("Default account not found for user"));
        }

        Transaction transaction = new Transaction();
        transaction.setTitle(dto.getTitle());
        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        transaction.setCategory(dto.getCategory());
        transaction.setDate(dto.getDate());
        transaction.setAccount(account);
        transaction.setCurrency(account.getCurrency());

        // Handle Allocation for Expenses
        if (dto.getType() == TransactionType.EXPENSE && dto.getFundingIncomeId() != null) {
            Transaction fundingIncome = transactionRepository.findById(dto.getFundingIncomeId())
                    .filter(t -> t.getType() == TransactionType.INCOME && t.getAccount().equals(account))
                    .orElseThrow(() -> new RuntimeException("Valid funding income not found in the selected account"));

            if (fundingIncome.getRemainingBalance().compareTo(dto.getAmount()) < 0) {
                throw new RuntimeException("Insufficient balance in selected income source. Available: "
                        + fundingIncome.getRemainingBalance());
            }

            fundingIncome.setAllocatedAmount(fundingIncome.getAllocatedAmount().add(dto.getAmount()));
            transaction.setFundingIncome(fundingIncome);
            transactionRepository.save(fundingIncome);
        }

        Transaction saved = transactionRepository.save(transaction);

        // Update account balance
        BigDecimal change = dto.getType() == TransactionType.INCOME ? dto.getAmount() : dto.getAmount().negate();
        accountService.updateBalance(account.getId(), change);

        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public TransactionDTO updateTransaction(Long id, TransactionUpdateDTO dto, User user) {
        validateDate(dto.getDate());
        Transaction transaction = transactionRepository.findById(id)
                .filter(t -> t.getAccount().getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        BigDecimal oldAmount = transaction.getAmount();
        TransactionType oldType = transaction.getType();
        FinancialAccount account = transaction.getAccount();
        Transaction oldFundingIncome = transaction.getFundingIncome();

        // Revert old allocation if it was an expense
        if (oldType == TransactionType.EXPENSE && oldFundingIncome != null) {
            oldFundingIncome.setAllocatedAmount(oldFundingIncome.getAllocatedAmount().subtract(oldAmount));
            transactionRepository.save(oldFundingIncome);
        }

        transaction.setTitle(dto.getTitle());
        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        transaction.setCategory(dto.getCategory());
        transaction.setDate(dto.getDate());

        // Apply new allocation if it's an expense
        if (dto.getType() == TransactionType.EXPENSE && dto.getFundingIncomeId() != null) {
            Transaction newFundingIncome = transactionRepository.findById(dto.getFundingIncomeId())
                    .filter(t -> t.getType() == TransactionType.INCOME && t.getAccount().equals(account))
                    .orElseThrow(() -> new RuntimeException("Valid funding income not found in the selected account"));

            if (newFundingIncome.getRemainingBalance().compareTo(dto.getAmount()) < 0) {
                throw new RuntimeException("Insufficient balance in selected income source. Available: "
                        + newFundingIncome.getRemainingBalance());
            }

            newFundingIncome.setAllocatedAmount(newFundingIncome.getAllocatedAmount().add(dto.getAmount()));
            transaction.setFundingIncome(newFundingIncome);
            transactionRepository.save(newFundingIncome);
        } else {
            transaction.setFundingIncome(null);
        }

        Transaction saved = transactionRepository.save(transaction);

        // Update account balance: remove old, add new
        BigDecimal oldChange = oldType == TransactionType.INCOME ? oldAmount.negate() : oldAmount;
        BigDecimal newChange = dto.getType() == TransactionType.INCOME ? dto.getAmount() : dto.getAmount().negate();
        accountService.updateBalance(account.getId(), oldChange.add(newChange));

        return mapToDTO(saved);
    }

    private void validateDate(LocalDateTime date) {
        if (date != null && date.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Future dates are not allowed. Please select today’s date or a past date.");
        }
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id, User user) {
        Transaction transaction = transactionRepository.findById(id)
                .filter(t -> t.getAccount().getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        // Revert allocation if it's an expense
        if (transaction.getType() == TransactionType.EXPENSE && transaction.getFundingIncome() != null) {
            Transaction fundingIncome = transaction.getFundingIncome();
            fundingIncome.setAllocatedAmount(fundingIncome.getAllocatedAmount().subtract(transaction.getAmount()));
            transactionRepository.save(fundingIncome);
        }

        // Update account balance
        BigDecimal change = transaction.getType() == TransactionType.INCOME ? transaction.getAmount().negate()
                : transaction.getAmount();
        accountService.updateBalance(transaction.getAccount().getId(), change);

        transactionRepository.delete(transaction);
    }

    @Override
    public List<TransactionDTO> getAllTransactions(User user, Long accountId) {
        if (accountId == null) {
            // Overall account logic
            List<FinancialAccount> accounts = accountRepository.findByUser(user).stream()
                    .filter(FinancialAccount::getIncludeInOverall)
                    .collect(Collectors.toList());
            return transactionRepository.findByAccountIn(accounts).stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        }
        FinancialAccount account = accountRepository.findByIdAndUser(accountId, user)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return transactionRepository.findByAccount(account).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDTO getTransactionById(Long id, User user) {
        Transaction transaction = transactionRepository.findById(id)
                .filter(t -> t.getAccount().getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        return mapToDTO(transaction);
    }

    @Override
    public List<TransactionDTO> getTransactionsByCategory(User user, Long accountId, Category category) {
        if (accountId == null) {
            List<FinancialAccount> accounts = accountRepository.findByUser(user).stream()
                    .filter(FinancialAccount::getIncludeInOverall)
                    .collect(Collectors.toList());
            return transactionRepository.findByAccountIn(accounts).stream()
                    .filter(t -> t.getCategory() == category)
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        }
        FinancialAccount account = accountRepository.findByIdAndUser(accountId, user)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return transactionRepository.findByAccountAndCategory(account, category).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MonthlySummaryDTO getMonthlySummary(User user, Long accountId, int year, int month) {
        List<Transaction> transactions;
        if (accountId == null) {
            List<FinancialAccount> accounts = accountRepository.findByUser(user).stream()
                    .filter(FinancialAccount::getIncludeInOverall)
                    .collect(Collectors.toList());
            transactions = transactionRepository.findByAccountsAndYearAndMonth(accounts, year, month);
        } else {
            FinancialAccount account = accountRepository.findByIdAndUser(accountId, user)
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            transactions = transactionRepository.findByAccountAndYearAndMonth(account, year, month);
        }

        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        Map<String, CategorySummaryDTO> categoryMap = new HashMap<>();

        for (Transaction t : transactions) {
            String categoryName = t.getCategory() != null ? t.getCategory().name() : "OTHER";
            CategorySummaryDTO catSummary = categoryMap.computeIfAbsent(categoryName, k -> new CategorySummaryDTO());

            if (t.getType() == TransactionType.INCOME) {
                totalIncome = totalIncome.add(t.getAmount());
                catSummary.setIncome(catSummary.getIncome().add(t.getAmount()));
            } else {
                totalExpense = totalExpense.add(t.getAmount().abs());
                catSummary.setExpense(catSummary.getExpense().add(t.getAmount().abs()));
            }
        }

        BigDecimal net = totalIncome.subtract(totalExpense);
        MonthlySummaryDTO dto = new MonthlySummaryDTO();
        dto.setYear(year);
        dto.setMonth(month);
        dto.setTotalIncome(totalIncome);
        dto.setTotalExpense(totalExpense);
        dto.setNet(net);
        dto.setCategories(categoryMap);
        return dto;
    }

    @Override
    public TotalsDTO getTotals(User user, Long accountId) {
        List<Transaction> transactions;
        if (accountId == null) {
            List<FinancialAccount> accounts = accountRepository.findByUser(user).stream()
                    .filter(FinancialAccount::getIncludeInOverall)
                    .collect(Collectors.toList());
            transactions = transactionRepository.findByAccountIn(accounts);
        } else {
            FinancialAccount account = accountRepository.findByIdAndUser(accountId, user)
                    .orElseThrow(() -> new RuntimeException("Account not found"));
            transactions = transactionRepository.findByAccount(account);
        }

        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalExpense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        TotalsDTO dto = new TotalsDTO();
        dto.setTotalIncome(totalIncome);
        dto.setTotalExpense(totalExpense);
        return dto;
    }

    private TransactionDTO mapToDTO(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setTitle(transaction.getTitle());
        dto.setAmount(transaction.getAmount());
        dto.setType(transaction.getType());
        dto.setCategory(transaction.getCategory());
        dto.setDate(transaction.getDate());

        // Allocation fields
        dto.setAllocatedAmount(transaction.getAllocatedAmount());
        dto.setRemainingBalance(transaction.getRemainingBalance());
        if (transaction.getFundingIncome() != null) {
            dto.setFundingIncomeId(transaction.getFundingIncome().getId());
            dto.setFundingIncomeTitle(transaction.getFundingIncome().getTitle());
        }

        // Add account info to DTO if needed for overall view
        if (transaction.getAccount() != null) {
            dto.setAccountId(transaction.getAccount().getId());
            dto.setAccountName(transaction.getAccount().getName());
        }
        dto.setCurrency(transaction.getCurrency());
        return dto;
    }

    @Override
    public List<TransactionDTO> getAvailableIncomes(User user, Long accountId) {
        List<FinancialAccount> accounts;
        if (accountId == null) {
            accounts = accountRepository.findByUser(user).stream()
                    .filter(FinancialAccount::getIncludeInOverall)
                    .collect(Collectors.toList());
        } else {
            accounts = List.of(accountRepository.findByIdAndUser(accountId, user)
                    .orElseThrow(() -> new RuntimeException("Account not found")));
        }

        return transactionRepository.findByAccountIn(accounts).stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(this::mapToDTO)
                .filter(dto -> dto.getRemainingBalance().compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.toList());
    }

}
