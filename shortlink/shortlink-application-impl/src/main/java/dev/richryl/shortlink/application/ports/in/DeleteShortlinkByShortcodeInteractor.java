package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;

public class DeleteShortlinkByShortcodeInteractor implements DeleteShortlinkByShortcodeUseCase{
    private final ShortlinkRepository shortlinkRepository;

    public DeleteShortlinkByShortcodeInteractor(ShortlinkRepository shortlinkRepository) {
        this.shortlinkRepository = shortlinkRepository;
    }

    @Override
    public void handle(String shortCode) {
        shortlinkRepository.deleteByShortCode(shortCode);
    }
}
