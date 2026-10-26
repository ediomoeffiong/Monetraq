package com.trasexp.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Category {
    FOOD, TRANSPORT, ENTERTAINMENT, UTILITIES, HEALTH, EDUCATION, OTHER;

    @JsonCreator
    public static Category fromString(String key) {
        if (key == null) return null;
        String normalized = key.trim().toLowerCase();
        return switch (normalized) {
            case "food" -> FOOD;
            case "transport", "transportation" -> TRANSPORT;
            case "entertainment" -> ENTERTAINMENT;
            case "utilities" -> UTILITIES;
            case "health" -> HEALTH;
            case "education" -> EDUCATION;
            case "other", "shopping", "salary", "freelance" -> OTHER;
            default -> {
                throw new IllegalArgumentException("Unknown category: " + key);
            }
        };
    }
}