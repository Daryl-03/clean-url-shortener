package shortlink;

import dev.richryl.bootstrap.ShortlinkApp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(
        classes = ShortlinkApp.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class ShortlinkE2ETest {

    @Autowired
    private WebTestClient webTestClient;

    private record ResponseData(EntityExchangeResult<Map<String, Object>> data) {
    }

    private ResponseData createShortlinkRequest(String requestBody) {
        EntityExchangeResult<Map<String, Object>> data = webTestClient.post()
                .uri("/api/shortlinks")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(new ParameterizedTypeReference<Map<String, Object>>() {
                }) // Deserialize the JSON into a Map
                .returnResult();
        return new ResponseData(data);
    }

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

    @Test
    @DisplayName("Retrieve previously created shortlink by id")
    void retrievePreviouslyCreatedShortlink() {
        String requestBody = """
                {
                     "url": "https://example.com/some/long/path"
                }
                """;
        ResponseData result = createShortlinkRequest(requestBody);

        assertNotNull(result.data().getResponseBody());

        String shortCode = (String) result.data().getResponseBody().get("shortCode");
        String id = (String) result.data().getResponseBody().get("id");

        assertNotNull(shortCode);

        webTestClient.get()
                .uri("/api/shortlinks/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.originalUrl").isEqualTo("https://example.com/some/long/path")
                .jsonPath("$.shortCode").isEqualTo(shortCode);
    }

    @Test
    @DisplayName("Update previously created shortlink by id")
    void updatePreviouslyCreatedShortlinkById() {
        String requestBody = """
                {
                    "url": "https://example.com/some/long/path"
                }
                """;

        ResponseData result = createShortlinkRequest(requestBody);

        assertNotNull(result.data().getResponseBody());

        String id = (String) result.data().getResponseBody().get("id");

        assertNotNull(id);

        String updateRequestBody = """
                {
                    "url": "https://example.com/updated/path"
                }
                """;

        webTestClient.put()
                .uri("/api/shortlinks/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateRequestBody)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.originalUrl").isEqualTo("https://example.com/updated/path");

    }

    @Test
    @DisplayName("When retrieving a non-existing shortlink, return 404 Not Found")
    void returnNotFoundForNonExistingShortlink() {
        UUID nonExistingId = UUID.randomUUID();

        webTestClient.get()
                .uri("/api/shortlinks/{nonExistingId}", nonExistingId)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").exists()
                .jsonPath("$.code").exists()
                .jsonPath("$.status").exists();
    }

    @Test
    @DisplayName("When id is invalid format, return 400 Bad Request with adequate error message")
    void returnBadRequestForInvalidIdFormat() {
        String invalidId = "invalid-uuid-format";
        webTestClient.get()
                .uri("/api/shortlinks/{id}", invalidId)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").exists()
                .jsonPath("$.code").exists()
                .jsonPath("$.status").exists();
    }

    @Test
    @DisplayName("Delete previously created shortlink")
    void deletePreviouslyCreatedShortlink() {
        String requestBody = """
                {
                    "url": "https://example.com/some/long/path"
                }
                """;

        ResponseData result = createShortlinkRequest(requestBody);

        assertNotNull(result.data().getResponseBody());

        String id = (String) result.data().getResponseBody().get("id");

        assertNotNull(id);

        webTestClient.delete()
                .uri("/api/shortlinks/{id}", id)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri("/api/shortlinks/{id}", id)
                .exchange()
                .expectStatus().isNotFound();
    }

}
