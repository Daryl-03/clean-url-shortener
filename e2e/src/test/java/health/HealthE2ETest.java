package health;

import dev.richryl.bootstrap.ShortlinkApp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
        classes = ShortlinkApp.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class HealthE2ETest {

    private final String HEALTH_ENDPOINT = "/api/health";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Should return status OK from health endpoint")
    void shouldReturnStatusOKFromHealthEndpoint() {
        webTestClient.get()
                .uri(HEALTH_ENDPOINT)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("OK");
    }
}
