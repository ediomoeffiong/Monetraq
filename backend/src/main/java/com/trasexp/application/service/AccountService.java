package com.trasexp.application.service;

import com.trasexp.application.dto.FinancialAccountCreateDTO;
import com.trasexp.application.dto.FinancialAccountDTO;
import com.trasexp.domain.User;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    List<FinancialAccountDTO> getUserAccounts(User user);

    FinancialAccountDTO getAccountById(Long id, User user);

    FinancialAccountDTO createAccount(FinancialAccountCreateDTO dto, User user);

    FinancialAccountDTO updateAccount(Long id, FinancialAccountCreateDTO dto, User user);

    void deleteAccount(Long id, User user);

    boolean verifyPin(Long id, String pin, User user);

    void updateBalance(Long id, BigDecimal amount);
}
