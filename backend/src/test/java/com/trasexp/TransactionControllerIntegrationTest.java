package com.trasexp;

import com.trasexp.domain.Category;
import com.trasexp.presentation.dto.TransactionRequest;
import com.trasexp.presentation.dto.TransactionResponse;
import com.trasexp.infrastructure.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Test
    void postTransaction_withFrontendValues_shouldSucceed() {
        var user = userRepository.findAll().stream().findFirst().orElseThrow();

        TransactionRequest request = new TransactionRequest();
        request.setTitle("Freelance job");
        request.setAmount(new BigDecimal("1200.00"));
        request.setType(com.trasexp.domain.TransactionType.INCOME);
        request.setCategory(Category.OTHER); // frontend sends 'Freelance' which maps to OTHER
        request.setDate(LocalDateTime.now());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<TransactionRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<TransactionResponse> resp = restTemplate.postForEntity("/api/transactions?userId=" + user.getId(), entity, TransactionResponse.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        TransactionResponse body = resp.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getType()).isEqualTo(com.trasexp.domain.TransactionType.INCOME);
        assertThat(body.getCategory()).isEqualTo(Category.OTHER);
    }

    @Test
    void postTransaction_fromFrontendPayload_shouldSucceed() {
        var user = userRepository.findAll().stream().findFirst().orElseThrow();

        String json = "{\"title\":\"Contract\",\"amount\":250.00,\"type\":\"income\",\"category\":\"OTHER\",\"date\":\"2026-01-30T00:00:00\"}";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        ResponseEntity<TransactionResponse> resp = restTemplate.postForEntity("/api/transactions?userId=" + user.getId(), entity, TransactionResponse.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        TransactionResponse body = resp.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getType()).isEqualTo(com.trasexp.domain.TransactionType.INCOME);
        assertThat(body.getCategory()).isEqualTo(Category.OTHER);
    }
}