package com.example.urlshortener.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class ShortUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalUrl;
    private String shortCode;
    private int clicks;

    private LocalDateTime createdAt;

    // Getters and Setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getOriginalUrl() { return originalUrl; }

    public void setOriginalUrl(String originalUrl) { this.originalUrl = originalUrl; }

    public String getShortCode() { return shortCode; }

    public void setShortCode(String shortCode) { this.shortCode = shortCode; }

    public int getClicks() { return clicks; }

    public void setClicks(int clicks) { this.clicks = clicks; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
