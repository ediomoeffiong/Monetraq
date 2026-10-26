package com.trasexp.presentation.dto;

import jakarta.validation.constraints.NotBlank;

public class AccountActionRequest {

    @NotBlank(message = "Password is required")
    private String password;
    private String pin;

    public AccountActionRequest() {
    }

    public AccountActionRequest(String password, String pin) {
        this.password = password;
        this.pin = pin;
    }

    // Getters and Setters
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
