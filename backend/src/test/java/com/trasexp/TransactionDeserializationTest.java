package com.trasexp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trasexp.domain.TransactionType;
import com.trasexp.presentation.dto.TransactionRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TransactionDeserializationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deserializeLowercaseEnum() throws Exception {
        String json = "{\"title\":\"Salary\",\"amount\":1000.00,\"type\":\"income\",\"category\":null,\"date\":\"2026-01-30T10:00:00\"}";

        TransactionRequest req = objectMapper.readValue(json, TransactionRequest.class);

        assertEquals(TransactionType.INCOME, req.getType());
    }
}