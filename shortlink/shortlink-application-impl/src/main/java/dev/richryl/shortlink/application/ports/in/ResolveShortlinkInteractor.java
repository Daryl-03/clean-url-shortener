package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.application.exceptions.ShortlinkNotFoundException;
import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;
import dev.richryl.shortlink.application.ports.out.AnalyticsPort;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;

public class ResolveShortlinkInteractor implements ResolveShortlinkUseCase{

    private final ShortlinkRepository shortlinkRepository;
    private final AnalyticsPort analyticsPort;

    public ResolveShortlinkInteractor(ShortlinkRepository shortlinkRepository, AnalyticsPort analyticsPort) {
        this.shortlinkRepository = shortlinkRepository;
        this.analyticsPort = analyticsPort;
    }

    @Override
    public ShortlinkResponse handle(String shortCode) throws ShortlinkNotFoundException {
        ShortlinkResponse shortlinkResponse = ShortlinkResponse.fromDomain(shortlinkRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ShortlinkNotFoundException("Shortlink not found for short code: " + shortCode)));

        analyticsPort.recordShortlinkAccess(shortlinkResponse.id());
        return shortlinkResponse;
    }
}
