package dev.richryl.identity.application.ports.in;

import dev.richryl.identity.application.ports.dto.ClickEventResponse;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface RetrieveRangedCuratedClickEventsUseCase {
    ClickEventStatResponse handle(UUID shortlinkId, Instant start, Instant end);
}
