package dev.richryl.analytics.application.ports.in;

import dev.richryl.identity.application.ports.dto.ClickEventResponse;
import dev.richryl.identity.application.ports.in.RetrieveRangedClickEventsUseCase;
import dev.richryl.identity.application.ports.out.ClickEventRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class RetrieveRangedClickEventsInteractor implements RetrieveRangedClickEventsUseCase {
    private final ClickEventRepository clickEventRepository;

    public RetrieveRangedClickEventsInteractor(ClickEventRepository clickEventRepository) {
        this.clickEventRepository = clickEventRepository;
    }

    @Override
    public List<ClickEventResponse> handle(UUID shortlinkId, Instant start, Instant end) {
        return clickEventRepository.findByTimestampBetween(shortlinkId, start, end).stream().map(event ->
                new ClickEventResponse(
                        event.getId(),
                        event.getTimestamp(),
                        event.getReferer(),
                        event.getLocation(),
                        event.getDevice()
                )
        ).toList();
    }
}
