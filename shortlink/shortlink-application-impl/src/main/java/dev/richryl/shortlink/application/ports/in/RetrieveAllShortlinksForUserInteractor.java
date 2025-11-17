package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;

import java.util.List;
import java.util.UUID;

public class RetrieveAllShortlinksForUserInteractor implements RetrieveAllShortlinksForUserUseCase {
    private final ShortlinkRepository shortlinkRepository;

    public RetrieveAllShortlinksForUserInteractor(ShortlinkRepository shortlinkRepository) {
        this.shortlinkRepository = shortlinkRepository;
    }

    @Override
    public List<ShortlinkResponse> handle(UUID userId) {
        return shortlinkRepository.findAllByOwnerId(userId);
    }
}
