package com.trasexp.application.service;

import com.trasexp.domain.LoginHistory;
import com.trasexp.domain.SecuritySession;
import com.trasexp.infrastructure.LoginHistoryRepository;
import com.trasexp.infrastructure.SecuritySessionRepository;
import com.trasexp.presentation.dto.LoginHistoryDTO;
import com.trasexp.presentation.dto.SecuritySessionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class SecurityService {

        private final SecuritySessionRepository securitySessionRepository;
        private final LoginHistoryRepository loginHistoryRepository;

        public List<SecuritySessionDTO> getActiveSessions(Long userId) {
                List<SecuritySession> sessions = securitySessionRepository
                                .findActiveSessionsByUserId(userId, LocalDateTime.now());

                return sessions.stream()
                                .map(session -> new SecuritySessionDTO(
                                                session.getId(),
                                                session.getIpAddress(),
                                                session.getUserAgent(),
                                                session.getCreatedAt(),
                                                session.getLastAccessedAt(),
                                                false // Can be enhanced to detect current session
                                ))
                                .collect(Collectors.toList());
        }

        @Transactional
        public void terminateSession(Long userId, Long sessionId) {
                SecuritySession session = securitySessionRepository.findByIdAndUserId(sessionId, userId)
                                .orElseThrow(() -> new RuntimeException("Session not found"));

                securitySessionRepository.delete(session);
        }

        public List<LoginHistoryDTO> getLoginHistory(Long userId, int limit) {
                List<LoginHistory> history = loginHistoryRepository
                                .findByUserIdOrderByLoginTimeDesc(userId, PageRequest.of(0, limit));

                return history.stream()
                                .map(record -> new LoginHistoryDTO(
                                                record.getId(),
                                                record.getIpAddress(),
                                                record.getUserAgent(),
                                                record.getLoginTime(),
                                                record.getSuccess(),
                                                record.getFailureReason()))
                                .collect(Collectors.toList());
        }

        @Transactional
        public void recordLogin(Long userId, String ipAddress, String userAgent, boolean success,
                        String failureReason) {
                LoginHistory record = new LoginHistory(userId, ipAddress, userAgent, success, failureReason);
                loginHistoryRepository.save(record);
        }

        @Transactional
        public void createSession(Long userId, String sessionToken, String ipAddress, String userAgent,
                        LocalDateTime expiresAt) {
                SecuritySession session = new SecuritySession(userId, sessionToken, ipAddress, userAgent, expiresAt);
                securitySessionRepository.save(session);
        }

        @Transactional
        public void cleanupExpiredSessions() {
                securitySessionRepository.deleteExpiredSessions(LocalDateTime.now());
        }
}
