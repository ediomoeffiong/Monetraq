package com.trasexp.application.service;

import com.trasexp.application.dto.*;
import com.trasexp.domain.Category;
import com.trasexp.domain.User;

import java.util.List;

public interface TransactionService {
    TransactionDTO createTransaction(TransactionCreateDTO dto, User user, Long accountId);

    TransactionDTO updateTransaction(Long id, TransactionUpdateDTO dto, User user);

    void deleteTransaction(Long id, User user);

    List<TransactionDTO> getAllTransactions(User user, Long accountId);

    TransactionDTO getTransactionById(Long id, User user);

    List<TransactionDTO> getTransactionsByCategory(User user, Long accountId, Category category);

    MonthlySummaryDTO getMonthlySummary(User user, Long accountId, int year, int month);

    TotalsDTO getTotals(User user, Long accountId);

    List<TransactionDTO> getAvailableIncomes(User user, Long accountId);
}