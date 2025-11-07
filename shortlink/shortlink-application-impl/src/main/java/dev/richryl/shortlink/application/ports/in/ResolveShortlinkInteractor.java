package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.exceptions.ShortlinkNotFoundException;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;

public class ResolveShortlinkInteractor implements ResolveShortlinkUseCase{

    private final ShortlinkRepository shortlinkRepository;

    public ResolveShortlinkInteractor(ShortlinkRepository shortlinkRepository) {
        this.shortlinkRepository = shortlinkRepository;
    }

    @Override
    public Shortlink handle(String shortCode) throws ShortlinkNotFoundException {
        return shortlinkRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ShortlinkNotFoundException("Shortlink not found for short code: " + shortCode));
    }
}
