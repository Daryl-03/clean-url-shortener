package dev.richryl.analytics.application.ports.dto;

import java.time.Instant;
import java.util.Map;

public record ClickPerDayPerDeviceTypeStat(
        Instant date,
        Map<String, Integer> countsPerDeviceType
) {
}

    