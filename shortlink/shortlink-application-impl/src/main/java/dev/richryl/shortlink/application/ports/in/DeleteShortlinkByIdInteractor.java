package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;

import java.util.UUID;

public class DeleteShortlinkByIdInteractor implements DeleteShortlinkByIdUseCase {
    private final ShortlinkRepository shortlinkRepository;

    public DeleteShortlinkByIdInteractor(ShortlinkRepository shortlinkRepository) {
        this.shortlinkRepository = shortlinkRepository;
    }

    @Override
    public void handle(UUID id) {
        shortlinkRepository.deleteById(id);
    }
}
