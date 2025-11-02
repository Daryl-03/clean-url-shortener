package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;
import dev.richryl.shortlink.application.ports.out.SlugGenerator;

public class CreateShortlinkInteractor implements CreateShortlinkUseCase {
    private final SlugGenerator slugGenerator;
        private final ShortlinkRepository shortlinkRepository;

    public CreateShortlinkInteractor(SlugGenerator slugGenerator, ShortlinkRepository shortlinkRepository) {
        this.slugGenerator = slugGenerator;
        this.shortlinkRepository = shortlinkRepository;

    }

    public Shortlink handle(String url){
        Shortlink shortlink = new Shortlink(url, slugGenerator.generate(url));
        shortlinkRepository.save(shortlink);
        return shortlink;
    }
}
