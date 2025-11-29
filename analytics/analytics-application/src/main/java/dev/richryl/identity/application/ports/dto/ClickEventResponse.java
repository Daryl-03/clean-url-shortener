package dev.richryl.identity.application.ports.dto;

import dev.richryl.analytics.domain.ClickEvent;
import dev.richryl.analytics.domain.DeviceInfo;
import dev.richryl.analytics.domain.GeoLocation;

import java.time.Instant;
import java.util.UUID;

public record ClickEventResponse(
        UUID id,
        Instant timestamp,
        String referer,
        GeoLocation location,
        DeviceInfo device

) {
    public static ClickEventResponse fromDomain(ClickEvent clickEvent) {
        return new ClickEventResponse(
                clickEvent.getId(),
                clickEvent.getTimestamp(),
                clickEvent.getReferer(),
                clickEvent.getLocation(),
                clickEvent.getDevice()
        );
    }
}
