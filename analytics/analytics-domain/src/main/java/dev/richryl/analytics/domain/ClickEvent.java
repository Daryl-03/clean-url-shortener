package dev.richryl.analytics.domain;

import java.time.Instant;
import java.util.UUID;

public class ClickEvent {
    private final UUID id;
    private final Instant timestamp;
    private final UUID shortlinkId;

    public ClickEvent(UUID uuid, Instant now, UUID firstShortlinkId) {
        this.id = uuid;
        this.timestamp = now;
        this.shortlinkId = firstShortlinkId;
    }

    public UUID getId() {
        return id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public UUID getShortlinkId() {
        return shortlinkId;
    }
}
