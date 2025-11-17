package analytics;

import dev.richryl.bootstrap.ShortlinkApp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import utils.AppConstants;
import utils.HelperMethod;

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
        String id = (String) createResponse.data().getResponseBody().get("id");
        String shortCode = (String) createResponse.data().getResponseBody().get("shortCode");

        // Resolve the shortlink multiple times to generate analytics data
        int resolveCount = 5;
        for (int i = 0; i < resolveCount; i++) {
            webTestClient.get()
                    .uri("/s/{shortcode}", shortCode)
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
                .jsonPath("$.[0].timestamp").exists();

    }
}
