package dev.richryl.analytics.application.ports.in;


import dev.richryl.analytics.application.ports.dto.ClickEventStatResponse;

import java.time.Instant;
import java.util.UUID;

public interface RetrieveRangedCuratedClickEventsUseCase {
    ClickEventStatResponse handle(UUID shortlinkId, Instant from, Instant to);
}
