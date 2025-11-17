package dev.richryl.identity.application.ports.dto;

import dev.richryl.analytics.domain.ClickEvent;

import java.time.Instant;
import java.util.UUID;

public record ClickEventResponse(
        UUID id,
        Instant timestamp
) {
    public static ClickEventResponse fromDomain(ClickEvent clickEvent) {
        return new ClickEventResponse(
                clickEvent.getId(),
                clickEvent.getTimestamp()
        );
    }
}
