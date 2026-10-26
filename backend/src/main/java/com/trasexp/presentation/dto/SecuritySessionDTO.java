package com.trasexp.presentation.dto;

import java.time.LocalDateTime;

public class SecuritySessionDTO {
    private Long id;
    private String ipAddress;
    private String userAgent;
    private String device;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime lastAccessedAt;
    private Boolean isCurrent;

    public SecuritySessionDTO() {
    }

    public SecuritySessionDTO(Long id, String ipAddress, String userAgent,
            LocalDateTime createdAt, LocalDateTime lastAccessedAt, Boolean isCurrent) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.createdAt = createdAt;
        this.lastAccessedAt = lastAccessedAt;
        this.isCurrent = isCurrent;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastAccessedAt() {
        return lastAccessedAt;
    }

    public void setLastAccessedAt(LocalDateTime lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }

    public Boolean getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(Boolean isCurrent) {
        this.isCurrent = isCurrent;
    }
}
