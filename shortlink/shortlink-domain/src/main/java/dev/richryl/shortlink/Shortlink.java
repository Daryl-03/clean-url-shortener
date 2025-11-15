package dev.richryl.shortlink;

import java.time.LocalDateTime;
import java.util.UUID;

public class Shortlink {
    private final UUID id;
    private final String originalUrl;
    private final String shortCode;
    private final UUID ownerId;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public Shortlink(UUID id, String originalUrl, String shortCode, UUID ownerId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public UUID getOwnerId() {
        return ownerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
