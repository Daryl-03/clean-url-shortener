package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.exceptions.ShortlinkNotFoundException;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;

import java.util.UUID;

public class GetShortlinkByIdInteractor implements GetShortlinkByIdUseCase {
    private final ShortlinkRepository shortlinkRepository;

    public GetShortlinkByIdInteractor(ShortlinkRepository shortlinkRepository) {
        this.shortlinkRepository = shortlinkRepository;
    }

    @Override
    public Shortlink handle(UUID shortlinkId) {
                return shortlinkRepository.findById(shortlinkId)
                .orElseThrow(() -> new ShortlinkNotFoundException("Shortlink not found for id: " + shortlinkId));
    }
}
