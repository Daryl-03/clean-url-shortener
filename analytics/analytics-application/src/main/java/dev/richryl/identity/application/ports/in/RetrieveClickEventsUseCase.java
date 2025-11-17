package dev.richryl.identity.application.ports.in;

import dev.richryl.identity.application.ports.dto.ClickEventResponse;

import java.util.List;
import java.util.UUID;

public interface RetrieveClickEventsUseCase {
    List<ClickEventResponse> handle(UUID urlId);
}
