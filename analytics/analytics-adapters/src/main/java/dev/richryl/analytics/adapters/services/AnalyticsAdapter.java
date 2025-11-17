package dev.richryl.analytics.adapters.services;

import dev.richryl.identity.application.ports.in.CreateClickEventUseCase;
import dev.richryl.shortlink.application.ports.out.AnalyticsPort;

import java.util.UUID;

public class AnalyticsAdapter implements AnalyticsPort {
    private final CreateClickEventUseCase createClickEventUseCase;
    public AnalyticsAdapter(CreateClickEventUseCase createClickEventUseCase) {
        this.createClickEventUseCase = createClickEventUseCase;
    }

    @Override
    public void recordShortlinkAccess(UUID shortlinkId) {
        createClickEventUseCase.handle(shortlinkId);
    }
}
