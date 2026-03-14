package dev.richryl.analytics.application.ports.in;

import dev.richryl.analytics.application.ports.dto.ClickEventResponse;

import java.util.List;
import java.util.UUID;

public interface RetrieveClickEventsUseCase {
    List<ClickEventResponse> handle(UUID urlId);
}
