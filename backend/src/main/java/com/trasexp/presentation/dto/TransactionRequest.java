package com.trasexp.presentation.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.trasexp.domain.Category;
import com.trasexp.domain.TransactionType;
import com.trasexp.presentation.config.LenientLocalDateTimeDeserializer;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionRequest {
    private String title;
    private BigDecimal amount;
    private TransactionType type;
    private Category category;

    @JsonDeserialize(using = LenientLocalDateTimeDeserializer.class)
    private LocalDateTime date;

    private Long accountId;
    private Long fundingIncomeId;
    private String currency;
}