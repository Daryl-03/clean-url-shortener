package dev.richryl.identity.application.ports.dto;

import java.util.UUID;

public record CreateClickEventCommand(
        UUID shortlinkId,
        String userAgent,
        String ipAddress,
        String referrer,
        String acceptLanguage
) {

}
