package dev.richryl.analytics.application.ports.dto;

import java.time.Instant;

public record ClickPerDayPerDeviceTypeStat(
        Instant day,
        String deviceType,
        int count
) {
}

    