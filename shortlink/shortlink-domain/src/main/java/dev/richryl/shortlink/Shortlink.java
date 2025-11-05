package dev.richryl.shortlink;

import java.util.UUID;

public class Shortlink {
    private final UUID id;
    private final String originalUrl;
    private final String shortCode;

    public Shortlink(UUID id, String originalUrl, String shortCode) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortCode() {
        return shortCode;
    }

    public UUID getId() {
        return id;
    }
}
