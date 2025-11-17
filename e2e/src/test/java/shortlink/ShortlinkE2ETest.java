package shortlink;

import dev.richryl.bootstrap.ShortlinkApp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import utils.AppConstants;
import utils.HelperMethod;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static utils.HelperMethod.createShortlinkRequest;

@SpringBootTest(
        classes = ShortlinkApp.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class ShortlinkE2ETest {

    private final String TOKEN = AppConstants.JWT_TOKEN;
    private final String DEFAULT_URL = "https://example.com/some/long/path";

    @Autowired
    private WebTestClient webTestClient;

    private WebTestClient.BodyContentSpec expectApiError(WebTestClient.BodyContentSpec body) {
        return body
                .jsonPath("$.error").exists()
                .jsonPath("$.code").exists()
                .jsonPath("$.status").exists();
    }

    @Test
    @DisplayName("E2E test for creating shortlink")
    void returnCreatedShortlinkWithCreatedStatus() {
        String requestBody = """
                {
                    "url": "%s"
                }
                """.formatted(DEFAULT_URL);

        getExchangePost("/api/shortlinks", requestBody)
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.originalUrl").isEqualTo(DEFAULT_URL)
                .jsonPath("$.shortCode").isNotEmpty();

    }

    private WebTestClient.ResponseSpec getExchangePost(String uri, String requestBody) {
        return webTestClient.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TOKEN)
                .bodyValue(requestBody)
                .exchange();
    }

    @Test
    @DisplayName("When an invalid url is provided, return 400 Bad Request with adequate error code and message")
    void returnBadRequestForInvalidUrl() {
        String requestBody = """
                {
                    "url": "invalid-url"
                }
                """;

        expectApiError(
                getExchangePost("/api/shortlinks", requestBody)
                        .expectStatus().isBadRequest()
                        .expectBody()
        );
    }

    @Test
    @DisplayName("Retrieve previously created shortlink by id")
    void retrievePreviouslyCreatedShortlink() {
        String requestBody = """
                {
                     "url": "%s"
                }
                """.formatted(DEFAULT_URL);
        HelperMethod.ResponseData result = createShortlinkRequest(requestBody, webTestClient);

        assertNotNull(result.data().getResponseBody());

        String shortCode = (String) result.data().getResponseBody().get("shortCode");
        String id = (String) result.data().getResponseBody().get("id");

        assertNotNull(shortCode);

        webTestClient.get()
                .uri("/api/shortlinks/{id}", id)
                .header("Authorization", "Bearer " + TOKEN)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.originalUrl").isEqualTo(DEFAULT_URL)
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

        HelperMethod.ResponseData result = createShortlinkRequest(requestBody, webTestClient);

        assertNotNull(result.data().getResponseBody());

        String id = (String) result.data().getResponseBody().get("id");

        assertNotNull(id);

        String updateRequestBody = """
                {
                    "id": "%s",
                    "url": "https://example.com/updated/path"
                }
                """;
        updateRequestBody = String.format(updateRequestBody, id);

        webTestClient.put()
                .uri("/api/shortlinks")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TOKEN)
                .bodyValue(updateRequestBody)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.originalUrl").isEqualTo("https://example.com/updated/path");

    }

    @Test
    @DisplayName("When updating a non-existing shortlink, return 404 Not Found")
    void returnNotFoundWhenUpdatingNonExistingShortlink() {
        String nonExistingId = UUID.randomUUID().toString();

        String updateRequestBody = """
                {
                    "id": "%s",
                    "url": "https://example.com/updated/path"
                }
                """;
        updateRequestBody = String.format(updateRequestBody, nonExistingId);

        webTestClient.put()
                .uri("/api/shortlinks")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TOKEN)
                .bodyValue(updateRequestBody)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.error").exists()
                .jsonPath("$.code").exists()
                .jsonPath("$.status").exists();
    }

    @Test
    @DisplayName("When retrieving a non-existing shortlink, return 404 Not Found")
    void returnNotFoundForNonExistingShortlink() {
        UUID nonExistingId = UUID.randomUUID();

        expectApiError(
                webTestClient.get()
                        .uri("/api/shortlinks/{nonExistingId}", nonExistingId)
                        .header("Authorization", "Bearer " + TOKEN)
                        .exchange()
                        .expectStatus().isNotFound()
                        .expectBody()
        );
    }

    @Test
    @DisplayName("When id is invalid format, return 400 Bad Request with adequate error message")
    void returnBadRequestForInvalidIdFormat() {
        String invalidId = "invalid-uuid-format";
        expectApiError(
                webTestClient.get()
                        .uri("/api/shortlinks/{id}", invalidId)
                        .header("Authorization", "Bearer " + TOKEN)
                        .exchange()
                        .expectStatus().isBadRequest()
                        .expectBody()
        );
    }

    @Test
    @DisplayName("Delete previously created shortlink")
    void deletePreviouslyCreatedShortlink() {
        String requestBody = """
                {
                    "url": "https://example.com/some/long/path"
                }
                """;

        HelperMethod.ResponseData result = createShortlinkRequest(requestBody, webTestClient);

        assertNotNull(result.data().getResponseBody());

        String id = (String) result.data().getResponseBody().get("id");

        assertNotNull(id);

        webTestClient.delete()
                .uri("/api/shortlinks/{id}", id)
                .header("Authorization", "Bearer " + TOKEN)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get()
                .uri("/api/shortlinks/{id}", id)
                .header("Authorization", "Bearer " + TOKEN)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @DisplayName("Redirect to original URL when requesting with short code")
    void redirectToOriginalUrlUsingShortCode() {
        String requestBody = """
                {
                    "url": "https://example.com/some/long/path"
                }
                """;

        HelperMethod.ResponseData result = createShortlinkRequest(requestBody, webTestClient);

        assertNotNull(result.data().getResponseBody());

        String shortCode = (String) result.data().getResponseBody().get("shortCode");

        assertNotNull(shortCode);

        webTestClient.get()
                .uri("/s/{shortCode}", shortCode)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", DEFAULT_URL);
    }

    @Test
    @DisplayName("should return a list of shortlinks for the authenticated user")
    void shouldReturnListOfShortlinksForAuthenticatedUser() {
        String requestBody1 = """
                {
                    "url": "https://example.com/first/path"
                }
                """;

        String requestBody2 = """
                {
                    "url": "https://example.com/second/path"
                }
                """;

        createShortlinkRequest(requestBody1, webTestClient);
        createShortlinkRequest(requestBody2, webTestClient);

        webTestClient.get()
                .uri("/api/shortlinks")
                .header("Authorization", "Bearer " + TOKEN)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].originalUrl").isNotEmpty()
                .jsonPath("$.[1].originalUrl").isNotEmpty();
    }
}
