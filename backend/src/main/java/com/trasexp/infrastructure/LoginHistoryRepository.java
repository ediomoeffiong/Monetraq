package com.trasexp.infrastructure;

import com.trasexp.domain.LoginHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

    @Query("SELECT l FROM LoginHistory l WHERE l.userId = :userId ORDER BY l.loginTime DESC")
    List<LoginHistory> findByUserIdOrderByLoginTimeDesc(@Param("userId") Long userId, Pageable pageable);

    void deleteByUserId(Long userId);
}
