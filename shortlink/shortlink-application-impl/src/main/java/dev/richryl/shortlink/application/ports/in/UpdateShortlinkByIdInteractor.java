package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.exceptions.ShortlinkNotFoundException;
import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;
import dev.richryl.shortlink.application.ports.dto.UpdateShortlinkCommand;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;

import java.time.Instant;

public class UpdateShortlinkByIdInteractor implements UpdateShortlinkByIdUseCase {
    private final ShortlinkRepository shortlinkRepository;


    public UpdateShortlinkByIdInteractor(ShortlinkRepository shortlinkRepository) {
        this.shortlinkRepository = shortlinkRepository;
    }

    @Override
    public ShortlinkResponse handle(UpdateShortlinkCommand command) {
        Shortlink shortlink = shortlinkRepository.findById(command.id())
                .orElseThrow(() -> new ShortlinkNotFoundException("Shortlink not found for id: " + command.id()));

        Shortlink updatedShortlink = new Shortlink(command.id(), command.url(), shortlink.getShortCode(), shortlink.getOwnerId(), shortlink.getCreatedAt(), Instant.now());
        shortlinkRepository.update(updatedShortlink);
        return ShortlinkResponse.fromDomain(updatedShortlink);
    }
}
