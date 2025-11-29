package analytics;

import dev.richryl.bootstrap.ShortlinkApp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import utils.AppConstants;
import utils.HelperMethod;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(
        classes = ShortlinkApp.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class AnalyticsE2ETest {
    private final String TOKEN = AppConstants.JWT_TOKEN;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("should retrieve analytics data for a shortlink")
    void shouldRetrieveAnalyticsDataForShortlink() {
        String url = "https://example.com/some/long/path";
        String requestBody = """
                {
                    "url": "%s"
                }
                """.formatted(url);

        // Create a shortlink first

        HelperMethod.ResponseData createResponse = HelperMethod.createShortlinkRequest(requestBody, webTestClient);
        assertNotNull(createResponse.data().getResponseBody());
        String id = (String) createResponse.data().getResponseBody().get("id");
        String shortCode = (String) createResponse.data().getResponseBody().get("shortCode");
        System.err.println("Created shortlink  1 with ID: " + id + " and shortCode: " + shortCode);
        String fakeUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3";

        int resolveCount = 5;
        for (int i = 0; i < resolveCount; i++) {
            webTestClient.get()
                    .uri("/s/{shortcode}", shortCode)
                    .header("User-Agent", fakeUserAgent)
                    .header("referer", "https://referrer.com/page")
                    .header("X-Forwarded-For", "10.10.101.10")
                    .exchange()
                    .expectStatus().is3xxRedirection();
        }


        // Retrieve analytics data
        webTestClient.get()
                .uri("/api/analytics/{id}", id)
                .header("Authorization", "Bearer " + TOKEN)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].id").exists()
                .jsonPath("$.[0].timestamp").exists()
                .jsonPath("$.[0].location").exists()
                .jsonPath("$.[0].referer").exists()
                .jsonPath("$.[0].device").exists()
                .jsonPath("$.[0].device.browser").isEqualTo("FakeBrowser");
    }

    @Test
    @DisplayName("should retrieve analytics data for a shortlink within date range")
    void shouldRetrieveAnalyticsDataForShortlinkWithinDateRange() {
        String url = "https://example.com/some/long/path";
        String requestBody = """
                {
                    "url": "%s"
                }
                """.formatted(url);

        // Create a shortlink first

        HelperMethod.ResponseData createResponse = HelperMethod.createShortlinkRequest(requestBody, webTestClient);
        assertNotNull(createResponse.data().getResponseBody());
        String id = (String) createResponse.data().getResponseBody().get("id");
        String shortCode = (String) createResponse.data().getResponseBody().get("shortCode");
        System.err.println("Created shortlink with ID: " + id + " and shortCode: " + shortCode);
        String fakeUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3";

        // Resolve the shortlink multiple times to generate analytics data
        int resolveCount = 5;
        for (int i = 0; i < resolveCount; i++) {
            webTestClient.get()
                    .uri("/s/{shortcode}", shortCode)
                    .header("User-Agent", fakeUserAgent)
                    .header("referer", "https://referrer.com/page")
                    .header("X-Forwarded-For", "10.10.101.10")
                    .exchange()
                    .expectStatus().is3xxRedirection();
        }



        // Retrieve analytics data
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/analytics/{id}/ranged")
                        .queryParam("from", Instant.now().minus(1, java.time.temporal.ChronoUnit.DAYS).toString())
                        .queryParam("to", Instant.now())
                        .build(id))
                .header("Authorization", "Bearer " + TOKEN)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].id").exists()
                .jsonPath("$.[0].timestamp").exists()
                .jsonPath("$.[0].location").exists()
                .jsonPath("$.[0].referer").exists()
                .jsonPath("$.[0].device").exists()
                .jsonPath("$.[0].device.browser").isEqualTo("FakeBrowser")
                .jsonPath("$.[4].id").exists();

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/analytics/{id}/ranged")
                        .queryParam("from", Instant.now().minus(2, java.time.temporal.ChronoUnit.DAYS).toString())
                        .queryParam("to", Instant.now().minus(1, java.time.temporal.ChronoUnit.DAYS).toString())
                        .build(id))
                .header("Authorization", "Bearer " + TOKEN)
                .exchange()
                .expectBody()
                .jsonPath("$.[0].id").doesNotExist();
    }
}
