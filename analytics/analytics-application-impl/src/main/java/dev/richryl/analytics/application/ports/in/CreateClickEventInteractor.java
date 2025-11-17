package dev.richryl.analytics.application.ports.in;

import dev.richryl.analytics.domain.ClickEvent;
import dev.richryl.identity.application.ports.in.CreateClickEventUseCase;
import dev.richryl.identity.application.ports.out.ClickEventIdGenerator;
import dev.richryl.identity.application.ports.out.ClickEventRepository;

import java.time.Instant;
import java.util.UUID;

public class CreateClickEventInteractor implements CreateClickEventUseCase {
    private final ClickEventRepository clickEventRepository;
    private final ClickEventIdGenerator clickEventIdGenerator;

    public CreateClickEventInteractor(ClickEventRepository clickEventRepository, ClickEventIdGenerator clickEventIdGenerator) {
        this.clickEventRepository = clickEventRepository;
        this.clickEventIdGenerator = clickEventIdGenerator;
    }


    @Override
    public void handle(UUID shortlinkId) {
        ClickEvent clickEvent = new ClickEvent(clickEventIdGenerator.generate(), Instant.now(),shortlinkId);
        clickEventRepository.save(clickEvent);
    }
}
