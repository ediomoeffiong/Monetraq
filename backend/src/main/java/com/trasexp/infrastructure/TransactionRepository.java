package com.trasexp.infrastructure;

import com.trasexp.domain.Transaction;
import com.trasexp.domain.FinancialAccount;
import com.trasexp.domain.TransactionType;
import com.trasexp.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

        List<Transaction> findByAccount(FinancialAccount account);

        List<Transaction> findByAccountIn(List<FinancialAccount> accounts);

        List<Transaction> findByAccountAndCategory(FinancialAccount account, Category category);

        List<Transaction> findByAccountAndType(FinancialAccount account, TransactionType type);

        @Query("SELECT t FROM Transaction t WHERE t.account = :account AND YEAR(t.date) = :year AND MONTH(t.date) = :month")
        List<Transaction> findByAccountAndYearAndMonth(@Param("account") FinancialAccount account,
                        @Param("year") int year,
                        @Param("month") int month);

        @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.account IN :accounts AND t.type = :type AND t.date BETWEEN :startDate AND :endDate")
        BigDecimal sumByAccountInAndTypeAndDateRange(
                        @Param("accounts") List<FinancialAccount> accounts,
                        @Param("type") TransactionType type,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        @Query("SELECT t FROM Transaction t WHERE t.account IN :accounts AND t.date BETWEEN :startDate AND :endDate")
        List<Transaction> findByAccountInAndDateRange(
                        @Param("accounts") List<FinancialAccount> accounts,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        @Query("SELECT t FROM Transaction t WHERE t.account IN :accounts AND YEAR(t.date) = :year AND MONTH(t.date) = :month")
        List<Transaction> findByAccountsAndYearAndMonth(@Param("accounts") List<FinancialAccount> accounts,
                        @Param("year") int year,
                        @Param("month") int month);

        @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.account = :account AND t.type = :type AND t.date BETWEEN :startDate AND :endDate")
        java.math.BigDecimal sumByAccountAndTypeAndDateRange(
                        @Param("account") FinancialAccount account,
                        @Param("type") TransactionType type,
                        @Param("startDate") java.time.LocalDateTime startDate,
                        @Param("endDate") java.time.LocalDateTime endDate);

        @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.account IN :accounts AND t.type = :type AND t.date BETWEEN :startDate AND :endDate")
        java.math.BigDecimal sumByAccountsAndTypeAndDateRange(
                        @Param("accounts") List<FinancialAccount> accounts,
                        @Param("type") TransactionType type,
                        @Param("startDate") java.time.LocalDateTime startDate,
                        @Param("endDate") java.time.LocalDateTime endDate);

        @Query("SELECT t FROM Transaction t WHERE t.account = :account AND t.date BETWEEN :startDate AND :endDate")
        List<Transaction> findByAccountAndDateRange(
                        @Param("account") FinancialAccount account,
                        @Param("startDate") java.time.LocalDateTime startDate,
                        @Param("endDate") java.time.LocalDateTime endDate);

        @Query("SELECT t FROM Transaction t WHERE t.account IN :accounts AND t.date BETWEEN :startDate AND :endDate")
        List<Transaction> findByAccountsAndDateRange(
                        @Param("accounts") List<FinancialAccount> accounts,
                        @Param("startDate") java.time.LocalDateTime startDate,
                        @Param("endDate") java.time.LocalDateTime endDate);

}