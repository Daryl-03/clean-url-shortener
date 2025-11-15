package dev.richryl.shortlink;

import java.util.UUID;

public class Shortlink {
    private final UUID id;
    private final String originalUrl;
    private final String shortCode;
    private final UUID ownerId;

    public Shortlink(UUID id, String originalUrl, String shortCode, UUID ownerId) {
        this.id = id;
        this.originalUrl = originalUrl;
        this.shortCode = shortCode;
        this.ownerId = ownerId;
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
}
