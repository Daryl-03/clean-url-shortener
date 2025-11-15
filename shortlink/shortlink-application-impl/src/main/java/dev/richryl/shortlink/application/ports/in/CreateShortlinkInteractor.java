package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;
import dev.richryl.shortlink.application.ports.out.ShortlinkIdGenerator;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;
import dev.richryl.shortlink.application.ports.out.SlugGenerator;

import java.util.UUID;

public class CreateShortlinkInteractor implements CreateShortlinkUseCase {
    private final SlugGenerator slugGenerator;
    private final ShortlinkRepository shortlinkRepository;
    private final ShortlinkIdGenerator shortlinkIdGenerator;

    public CreateShortlinkInteractor(SlugGenerator slugGenerator, ShortlinkRepository shortlinkRepository, ShortlinkIdGenerator shortlinkIdGenerator) {
        this.slugGenerator = slugGenerator;
        this.shortlinkRepository = shortlinkRepository;
        this.shortlinkIdGenerator = shortlinkIdGenerator;
    }

    public ShortlinkResponse handle(String url, UUID ownerId) {
        Shortlink shortlink = new Shortlink(shortlinkIdGenerator.generate(), url, slugGenerator.generate(url), ownerId);
        shortlinkRepository.save(shortlink);
        return ShortlinkResponse.fromDomain(shortlink);
    }
}
