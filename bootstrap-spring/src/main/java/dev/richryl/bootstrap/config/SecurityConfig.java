package dev.richryl.bootstrap.config;

import dev.richryl.identity.application.ports.in.CreateUserUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPublicKey;
import java.util.Collections;

@Configuration
public class SecurityConfig {
    private final RSAPublicKey key;
    private final CreateUserUseCase createUserUseCase;

    public SecurityConfig(CreateUserUseCase createUserUseCase) throws Exception {
        key = KeyLoader.loadPublicKey();
        this.createUserUseCase = createUserUseCase;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // authorize options
        http
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults())
            );
        return http.build();
    }

    @Bean
    @Profile("local")
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withPublicKey(this.key).build();

        MappedJwtClaimSetConverter converter = MappedJwtClaimSetConverter
                .withDefaults(Collections.singletonMap("sub",
                        (claimValue) -> {
                            String externalId = (String) claimValue;
                            return createUserUseCase.handle(externalId).id();
                        }
                ));
        jwtDecoder.setClaimSetConverter(converter);

        return jwtDecoder;
    }

    @Bean
    @Profile("prod")
    public JwtDecoder prodJwtDecoder() {
        String issuerLocation = System.getenv("OAUTH2_ISSUER_URI");
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withIssuerLocation(issuerLocation).build();

        MappedJwtClaimSetConverter converter = MappedJwtClaimSetConverter
                .withDefaults(Collections.singletonMap("sub",
                        (claimValue) -> {
                            String externalId = (String) claimValue;
                            return createUserUseCase.handle(externalId).id();
                        }
                ));
        jwtDecoder.setClaimSetConverter(converter);

        return jwtDecoder;
    }
}
