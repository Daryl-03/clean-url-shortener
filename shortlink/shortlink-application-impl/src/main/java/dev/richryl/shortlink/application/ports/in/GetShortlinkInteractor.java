package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.exceptions.ShortlinkNotFoundException;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;

public class GetShortlinkInteractor implements GetShortlinkUseCase {
    private final ShortlinkRepository shortlinkRepository;

    public GetShortlinkInteractor(ShortlinkRepository shortlinkRepository) {
        this.shortlinkRepository = shortlinkRepository;
    }

    @Override
    public Shortlink handle(String shortCode) {
                return shortlinkRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ShortlinkNotFoundException("Shortlink not found for code: " + shortCode));
    }
}
