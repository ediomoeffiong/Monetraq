package com.trasexp.presentation.dto;

import java.time.LocalDateTime;

public class LoginHistoryDTO {
    private Long id;
    private String ipAddress;
    private String userAgent;
    private String device;
    private String location;
    private LocalDateTime loginTime;
    private Boolean success;
    private String failureReason;

    public LoginHistoryDTO() {
    }

    public LoginHistoryDTO(Long id, String ipAddress, String userAgent,
            LocalDateTime loginTime, Boolean success, String failureReason) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.loginTime = loginTime;
        this.success = success;
        this.failureReason = failureReason;
        this.device = parseDevice(userAgent);
        this.location = "Unknown"; // Can be enhanced with IP geolocation
    }

    private String parseDevice(String userAgent) {
        if (userAgent == null)
            return "Unknown";
        if (userAgent.contains("Mobile"))
            return "Mobile";
        if (userAgent.contains("Tablet"))
            return "Tablet";
        return "Desktop";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }
}
