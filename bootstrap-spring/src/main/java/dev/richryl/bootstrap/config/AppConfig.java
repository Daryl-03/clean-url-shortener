package dev.richryl.bootstrap.config;

import dev.richryl.shortlink.adapters.persistence.InMemoryShortlinkRepository;
import dev.richryl.shortlink.adapters.services.Base62SlugGenerator;
import dev.richryl.shortlink.adapters.services.UuidIdGenerator;
import dev.richryl.shortlink.application.ports.in.*;
import dev.richryl.shortlink.application.ports.out.ShortlinkIdGenerator;
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
    public ShortlinkIdGenerator shortlinkIdGenerator() {
        return new UuidIdGenerator();
    }

    @Bean
    public CreateShortlinkUseCase createShortlinkUseCase(SlugGenerator slugGenerator, ShortlinkRepository shortlinkRepository, ShortlinkIdGenerator shortlinkIdGenerator) {
        return new CreateShortlinkInteractor(slugGenerator, shortlinkRepository, shortlinkIdGenerator);
    }

    @Bean
    public ShortlinkRepository shortlinkRepository() {
        return new InMemoryShortlinkRepository();
    }

    @Bean
    public GetShortlinkByIdUseCase getShortlinkByIdUseCase(ShortlinkRepository shortlinkRepository) {
        return new GetShortlinkByIdInteractor(shortlinkRepository);
    }

    @Bean
    public DeleteShortlinkByIdUseCase deleteShortlinkByIdUseCase(ShortlinkRepository shortlinkRepository) {
        return new DeleteShortlinkByIdInteractor(shortlinkRepository);
    }

    @Bean
    public UpdateShortlinkByIdUseCase updateShortlinkByIdUseCase(ShortlinkRepository shortlinkRepository) {
        return new UpdateShortlinkByIdInteractor(shortlinkRepository);
    }

    @Bean
    public ResolveShortlinkUseCase resolveShortlinkUseCase(ShortlinkRepository shortlinkRepository) {
        return new ResolveShortlinkInteractor(shortlinkRepository);
    }
}
