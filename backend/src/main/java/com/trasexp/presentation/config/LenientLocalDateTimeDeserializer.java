package com.trasexp.presentation.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class LenientLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        if (text == null || text.isBlank()) return null;

        try {
            // Try full LocalDateTime first
            return LocalDateTime.parse(text);
        } catch (DateTimeParseException ex) {
            try {
                // Fallback to LocalDate (date-only)
                LocalDate date = LocalDate.parse(text);
                return date.atStartOfDay();
            } catch (DateTimeParseException ex2) {
                throw new JsonParseException(p, "Failed to parse LocalDateTime: " + text);
            }
        }
    }
}