package com.trasexp.application.service;

import com.trasexp.application.dto.FinancialAccountCreateDTO;
import com.trasexp.application.dto.FinancialAccountDTO;
import com.trasexp.domain.FinancialAccount;
import com.trasexp.domain.User;
import com.trasexp.infrastructure.FinancialAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final FinancialAccountRepository accountRepository;
    private final com.trasexp.infrastructure.TransactionRepository transactionRepository;
    private final CurrencyConversionService conversionService;

    @Override
    public List<FinancialAccountDTO> getUserAccounts(User user) {
        return accountRepository.findByUser(user).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FinancialAccountDTO getAccountById(Long id, User user) {
        return accountRepository.findByIdAndUser(id, user)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Override
    @Transactional
    public FinancialAccountDTO createAccount(FinancialAccountCreateDTO dto, User user) {
        FinancialAccount account = new FinancialAccount();
        account.setName(dto.getName());
        account.setType(dto.getType());
        account.setDescription(dto.getDescription());
        account.setPin(dto.getPin());
        account.setIsDefault(dto.getIsDefault());
        account.setPinRequired(dto.getPinRequired());
        account.setCurrency(dto.getCurrency() != null ? dto.getCurrency() : "NGN");
        account.setIncludeInOverall(dto.getIncludeInOverall() != null ? dto.getIncludeInOverall() : true);
        account.setUser(user);
        account.setBalance(BigDecimal.ZERO);

        if (Boolean.TRUE.equals(account.getIsDefault())) {
            // Unset other default accounts
            accountRepository.findByUserAndIsDefaultTrue(user)
                    .ifPresent(a -> {
                        a.setIsDefault(false);
                        accountRepository.save(a);
                    });
        }
        return mapToDTO(accountRepository.save(account));
    }

    @Override
    @Transactional
    public FinancialAccountDTO updateAccount(Long id, FinancialAccountCreateDTO dto, User user) {
        FinancialAccount account = accountRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        String oldCurrency = account.getCurrency();
        String newCurrency = dto.getCurrency();

        account.setName(dto.getName());
        account.setType(dto.getType());
        account.setDescription(dto.getDescription());
        if (dto.getPin() != null && !dto.getPin().isEmpty()) {
            account.setPin(dto.getPin());
        }
        account.setPinRequired(dto.getPinRequired());
        if (dto.getIncludeInOverall() != null) {
            account.setIncludeInOverall(dto.getIncludeInOverall());
        }

        // Handle Currency Change & Conversion
        if (newCurrency != null && !newCurrency.equals(oldCurrency)) {
            // Convert Balance
            BigDecimal oldBalance = account.getBalance();
            BigDecimal newBalance = conversionService.convert(oldBalance, oldCurrency, newCurrency);
            account.setBalance(newBalance);
            account.setCurrency(newCurrency);

            // Convert All Transactions for this account
            List<com.trasexp.domain.Transaction> transactions = transactionRepository.findByAccount(account);
            for (com.trasexp.domain.Transaction t : transactions) {
                BigDecimal oldAmount = t.getAmount();
                BigDecimal newAmount = conversionService.convert(oldAmount, oldCurrency, newCurrency);
                t.setAmount(newAmount);
                t.setCurrency(newCurrency);

                // Also convert allocated amount if it's an income
                if (t.getType() == com.trasexp.domain.TransactionType.INCOME) {
                    BigDecimal oldAllocated = t.getAllocatedAmount();
                    BigDecimal newAllocated = conversionService.convert(oldAllocated, oldCurrency, newCurrency);
                    t.setAllocatedAmount(newAllocated);
                }
                transactionRepository.save(t);
            }
        }

        if (Boolean.TRUE.equals(dto.getIsDefault()) && !Boolean.TRUE.equals(account.getIsDefault())) {
            accountRepository.findByUserAndIsDefaultTrue(user)
                    .ifPresent(a -> {
                        a.setIsDefault(false);
                        accountRepository.save(a);
                    });
            account.setIsDefault(true);
        }

        return mapToDTO(accountRepository.save(account));
    }

    @Override
    @Transactional
    public void deleteAccount(Long id, User user) {
        FinancialAccount account = accountRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (Boolean.TRUE.equals(account.getIsDefault())) {
            throw new RuntimeException("Cannot delete default account");
        }
        accountRepository.delete(account);
    }

    @Override
    public boolean verifyPin(Long id, String pin, User user) {
        FinancialAccount account = accountRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (!Boolean.TRUE.equals(account.getPinRequired())) {
            return true;
        }
        return account.getPin() != null && account.getPin().equals(pin);
    }

    @Override
    @Transactional
    @SuppressWarnings("null")
    public void updateBalance(Long id, BigDecimal amount) {
        FinancialAccount account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }

    private FinancialAccountDTO mapToDTO(FinancialAccount account) {
        FinancialAccountDTO dto = new FinancialAccountDTO();
        dto.setId(account.getId());
        dto.setName(account.getName());
        dto.setType(account.getType());
        dto.setDescription(account.getDescription());
        dto.setBalance(account.getBalance());
        dto.setIsDefault(account.getIsDefault());
        dto.setPinRequired(account.getPinRequired());
        dto.setCurrency(account.getCurrency());
        dto.setIncludeInOverall(account.getIncludeInOverall());
        return dto;
    }
}
