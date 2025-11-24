package dev.richryl.bootstrap.config;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.context.annotation.Profile;
    import org.springframework.web.servlet.config.annotation.CorsRegistry;
    import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

    @Configuration
    @Profile("!local")
    public class ProdCorsConfiguration implements WebMvcConfigurer {

        private static final Logger logger = LoggerFactory.getLogger(ProdCorsConfiguration.class);

        private final String allowedOrigins;

        public ProdCorsConfiguration() {
            this.allowedOrigins = System.getenv("CORS_ALLOWED_ORIGINS");
            if (allowedOrigins == null || allowedOrigins.trim().isEmpty()) {
                logger.warn("The CORS_ALLOWED_ORIGINS environment variable is not set or is empty. No origins will be allowed for CORS.");
            } else {
                logger.info("Cors allowed origins set to: {}", allowedOrigins);
            }
        }

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/api/**")
                    .allowedOrigins(allowedOrigins != null ? allowedOrigins.split(",") : new String[]{})
                    .allowedMethods("POST", "GET", "PUT", "DELETE")
                    .allowedHeaders("*")
                    .allowCredentials(true);
        }
    }