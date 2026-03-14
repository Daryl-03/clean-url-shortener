package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.application.exceptions.ShortlinkNotFoundException;
import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;

public class GetShortlinkByShortcodeInteractor implements GetShortlinkByShortcodeUseCase{
    private final ShortlinkRepository shortlinkRepository;

    public GetShortlinkByShortcodeInteractor(ShortlinkRepository shortlinkRepository) {
        this.shortlinkRepository = shortlinkRepository;
    }

    @Override
    public ShortlinkResponse handle(String shortcode) throws ShortlinkNotFoundException {
        return ShortlinkResponse.fromDomain(shortlinkRepository.findByShortCode(shortcode).orElseThrow(() -> new ShortlinkNotFoundException("Shortlink with shortcode " + shortcode + " not found")));
    }
}
