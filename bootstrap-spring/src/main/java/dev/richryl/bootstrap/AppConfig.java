package dev.richryl.bootstrap;

import dev.richryl.shortlink.adapaters.services.Base62SlugGenerator;
import dev.richryl.shortlink.application.ports.in.CreateShortlinkInteractor;
import dev.richryl.shortlink.application.ports.in.CreateShortlinkUseCase;
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
    public CreateShortlinkUseCase createShortlinkUseCase(SlugGenerator slugGenerator) {
        return new CreateShortlinkInteractor(slugGenerator);
    }
}
