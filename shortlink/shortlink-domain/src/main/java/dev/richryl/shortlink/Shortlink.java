package dev.richryl.shortlink;

import java.time.Instant;
import java.util.UUID;

public class Shortlink {
    private final UUID id;
    private final String originalUrl;
    private final String shortCode;
    private final UUID ownerId;
    private final Instant createdAt;
    private final Instant updatedAt;

    public Shortlink(UUID id, String originalUrl, String shortCode, UUID ownerId, Instant createdAt, Instant updatedAt) {
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
