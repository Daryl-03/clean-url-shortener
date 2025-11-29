package dev.richryl.analytics.domain;

import java.time.Instant;
import java.util.UUID;

public class ClickEvent {
    private final UUID id;
    private final Instant timestamp;
    private final UUID shortlinkId;
    private final String referer;
    private final GeoLocation location;
    private final DeviceInfo device;

    public ClickEvent(UUID uuid, Instant now, UUID firstShortlinkId, String referer, GeoLocation location, DeviceInfo device) {
        this.id = uuid;
        this.timestamp = now;
        this.shortlinkId = firstShortlinkId;
        this.referer = referer;
        this.location = location;
        this.device = device;
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

    public String getReferer() {
        return referer;
    }

    public GeoLocation getLocation() {
        return location;
    }

    public DeviceInfo getDevice() {
        return device;
    }

}
