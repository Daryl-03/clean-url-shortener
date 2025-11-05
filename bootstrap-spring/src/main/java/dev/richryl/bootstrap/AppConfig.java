package dev.richryl.bootstrap;

import dev.richryl.shortlink.adapters.persistence.InMemoryShortlinkRepository;
import dev.richryl.shortlink.adapters.services.Base62SlugGenerator;
import dev.richryl.shortlink.application.ports.in.*;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;
import dev.richryl.shortlink.application.ports.out.SlugGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public SlugGenerator slugGenerator() {
        return new Base62SlugGenerator();
    }

    @Bean
    public CreateShortlinkUseCase createShortlinkUseCase(SlugGenerator slugGenerator, ShortlinkRepository shortlinkRepository) {
        return new CreateShortlinkInteractor(slugGenerator, shortlinkRepository);
    }

    @Bean
    public ShortlinkRepository shortlinkRepository() {
        return new InMemoryShortlinkRepository();
    }

    @Bean
    public GetShortlinkByShortcodeUseCase getShortlinkByShortcodeUseCase(ShortlinkRepository shortlinkRepository) {
        return new GetShortlinkByShortcodeInteractor(shortlinkRepository);
    }

    @Bean
    public DeleteShortlinkByShortcodeUseCase deleteShortlinkByShortcodeUseCase(ShortlinkRepository shortlinkRepository) {
        return new DeleteShortlinkByShortcodeInteractor(shortlinkRepository);
    }
}
