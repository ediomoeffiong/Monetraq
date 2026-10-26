package com.trasexp.infrastructure;

import com.trasexp.domain.SecuritySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SecuritySessionRepository extends JpaRepository<SecuritySession, Long> {

    @Query("SELECT s FROM SecuritySession s WHERE s.userId = :userId AND s.expiresAt > :now ORDER BY s.lastAccessedAt DESC")
    List<SecuritySession> findActiveSessionsByUserId(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    Optional<SecuritySession> findByIdAndUserId(Long id, Long userId);

    Optional<SecuritySession> findBySessionToken(String sessionToken);

    void deleteByUserId(Long userId);

    @Query("DELETE FROM SecuritySession s WHERE s.expiresAt < :now")
    void deleteExpiredSessions(@Param("now") LocalDateTime now);
}
