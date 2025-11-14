package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.application.exceptions.ShortlinkNotFoundException;
import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;

import java.util.UUID;

public class GetShortlinkByIdInteractor implements GetShortlinkByIdUseCase {
    private final ShortlinkRepository shortlinkRepository;

    public GetShortlinkByIdInteractor(ShortlinkRepository shortlinkRepository) {
        this.shortlinkRepository = shortlinkRepository;
    }

    @Override
    public ShortlinkResponse handle(UUID shortlinkId) {
                return ShortlinkResponse.fromDomain(shortlinkRepository.findById(shortlinkId)
                .orElseThrow(() -> new ShortlinkNotFoundException("Shortlink not found for id: " + shortlinkId)));
    }
}
