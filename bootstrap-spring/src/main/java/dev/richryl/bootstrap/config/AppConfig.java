package dev.richryl.bootstrap.config;

import dev.richryl.common.adapters.services.Slf4jLoggerAdapter;
import dev.richryl.common.adapters.services.UuidIdGenerator;
import dev.richryl.identity.adapters.persistence.InMemoryUserRepository;
import dev.richryl.identity.application.ports.in.CreateUserInteractor;
import dev.richryl.identity.application.ports.in.CreateUserUseCase;
import dev.richryl.identity.application.ports.in.RetrieveUserInfoInteractor;
import dev.richryl.identity.application.ports.in.RetrieveUserInfoUseCase;
import dev.richryl.identity.application.ports.out.LoggerPort;
import dev.richryl.identity.application.ports.out.UserIdGenerator;
import dev.richryl.identity.application.ports.out.UserRepository;
import dev.richryl.shortlink.adapters.persistence.InMemoryShortlinkRepository;
import dev.richryl.shortlink.adapters.services.Base62SlugGenerator;
import dev.richryl.shortlink.application.ports.in.*;
import dev.richryl.shortlink.application.ports.out.ShortlinkIdGenerator;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;
import dev.richryl.shortlink.application.ports.out.SlugGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


@Configuration
public class AppConfig {

    @Bean
    public SlugGenerator slugGenerator() {
        return new Base62SlugGenerator();
    }

    @Bean
    @Primary
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

    @Bean
    public LoggerPort loggerPort() {
        return new Slf4jLoggerAdapter();
    }

    @Bean
    public UserRepository userRepository() {
        return new InMemoryUserRepository();
    }

    @Bean
    public RetrieveUserInfoUseCase retrieveUserInfoUseCase(UserRepository userRepository, LoggerPort loggerPort) {
        return new RetrieveUserInfoInteractor(
                userRepository,
                loggerPort
        );
    }

    @Bean
    public UserIdGenerator idGenerator() {
        return new UuidIdGenerator();
    }


    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository userRepository, UserIdGenerator userIdGenerator) {
        return new CreateUserInteractor(userRepository, userIdGenerator);
    }
}
