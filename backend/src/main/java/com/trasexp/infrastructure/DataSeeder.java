package com.trasexp.infrastructure;

import com.trasexp.domain.Transaction;
import com.trasexp.domain.FinancialAccount;
import com.trasexp.domain.AccountType;
import com.trasexp.domain.TransactionType;
import com.trasexp.domain.Category;
import com.trasexp.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@Profile("dev")
@RequiredArgsConstructor
@SuppressWarnings("null")
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final FinancialAccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Create sample user
        User user = new User();
        user.setUsername("sampleuser");
        user.setEmail("sample@example.com");
        user.setPassword(passwordEncoder.encode("password123"));
        user = userRepository.save(user);

        // Create default account
        FinancialAccount account = new FinancialAccount();
        account.setName("Primary Savings");
        account.setType(AccountType.SAVINGS);
        account.setUser(user);
        account.setIsDefault(true);
        account.setBalance(new BigDecimal("5000.00"));
        account = accountRepository.save(account);

        // Create sample transactions
        Transaction income1 = new Transaction();
        income1.setTitle("Salary");
        income1.setAmount(new BigDecimal("5000.00"));
        income1.setType(TransactionType.INCOME);
        income1.setCategory(Category.OTHER);
        income1.setDate(LocalDateTime.now().minusDays(10));
        income1.setAccount(account);

        Transaction expense1 = new Transaction();
        expense1.setTitle("Groceries");
        expense1.setAmount(new BigDecimal("150.00"));
        expense1.setType(TransactionType.EXPENSE);
        expense1.setCategory(Category.FOOD);
        expense1.setDate(LocalDateTime.now().minusDays(5));
        expense1.setAccount(account);

        Transaction expense2 = new Transaction();
        expense2.setTitle("Bus Ticket");
        expense2.setAmount(new BigDecimal("20.00"));
        expense2.setType(TransactionType.EXPENSE);
        expense2.setCategory(Category.TRANSPORT);
        expense2.setDate(LocalDateTime.now().minusDays(2));
        expense2.setAccount(account);

        transactionRepository.save(income1);
        transactionRepository.save(expense1);
        transactionRepository.save(expense2);

        System.out.println("Sample data seeded for development.");
    }
}