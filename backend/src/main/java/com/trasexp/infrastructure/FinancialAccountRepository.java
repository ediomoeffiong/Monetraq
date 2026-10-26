package com.trasexp.infrastructure;

import com.trasexp.domain.FinancialAccount;
import com.trasexp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialAccountRepository extends JpaRepository<FinancialAccount, Long> {
    List<FinancialAccount> findByUser(User user);

    Optional<FinancialAccount> findByIdAndUser(Long id, User user);

    Optional<FinancialAccount> findByUserAndIsDefaultTrue(User user);
}
