package shortlink;

import dev.richryl.bootstrap.ShortlinkApp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(
        classes = ShortlinkApp.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class ShortlinkE2ETest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    @DisplayName("E2E test for creating shortlink")
    void returnCreatedShortlinkWithCreatedStatus() {
        String requestBody = """
                {
                    "url": "https://example.com/some/long/path"
                }
                """;

        webTestClient.post()
                .uri("/api/shortlinks")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.originalUrl").isEqualTo("https://example.com/some/long/path")
                .jsonPath("$.shortCode").isNotEmpty();

    }

    @Test
    @DisplayName("When an invalid url is provided, return 400 Bad Request with adequate error code and message")
    void returnBadRequestForInvalidUrl() {
        String requestBody = """
                {
                    "url": "invalid-url"
                }
                """;

        webTestClient.post()
                .uri("/api/shortlinks")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").exists()
                .jsonPath("$.code").exists()
                .jsonPath("$.status").exists();
    }

}
