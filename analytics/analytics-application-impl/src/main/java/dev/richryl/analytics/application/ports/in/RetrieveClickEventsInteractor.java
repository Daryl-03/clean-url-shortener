package dev.richryl.analytics.application.ports.in;

import dev.richryl.identity.application.ports.dto.ClickEventResponse;
import dev.richryl.identity.application.ports.in.RetrieveClickEventsUseCase;
import dev.richryl.identity.application.ports.out.ClickEventRepository;

import java.util.List;
import java.util.UUID;

public class RetrieveClickEventsInteractor implements RetrieveClickEventsUseCase {
    private final ClickEventRepository clickEventRepository;

    public RetrieveClickEventsInteractor(ClickEventRepository clickEventRepository) {
        this.clickEventRepository = clickEventRepository;
    }

    @Override
    public List<ClickEventResponse> handle(UUID urlId) {
        return clickEventRepository.findByShortlinkId(urlId).stream()
                .map(ClickEventResponse::fromDomain)
                .toList();
    }
}
