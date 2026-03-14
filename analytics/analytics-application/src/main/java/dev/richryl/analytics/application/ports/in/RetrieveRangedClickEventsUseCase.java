package dev.richryl.analytics.application.ports.in;


import dev.richryl.analytics.application.ports.dto.ClickEventResponse;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface RetrieveRangedClickEventsUseCase {
    List<ClickEventResponse> handle(UUID shortlinkId, Instant start, Instant end);
}
