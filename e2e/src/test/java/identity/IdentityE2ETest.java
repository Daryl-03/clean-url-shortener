package identity;


import dev.richryl.bootstrap.ShortlinkApp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import utils.AppConstants;


import java.util.Map;

@SpringBootTest(
        classes = ShortlinkApp.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class IdentityE2ETest {

    private final String TOKEN = AppConstants.JWT_TOKEN;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Requests to api/shortlinks/* with none or invalid jwt token should be disallowed ")
    void requestsWithInvalidJwtTokenShouldBeDisallowed(){
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
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Requests to api/shortlinks/* with valid jwt token should be allowed ")
    void requestsWithValidJwtTokenShouldBeAllowed(){

        String requestBody = """
                {
                    "url": "https://example.com/some/long/path"
                }
                """;

        webTestClient.post()
                .uri("/api/shortlinks")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TOKEN)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isCreated();

    }

    @Test
    @DisplayName("Requests to s/* without jwt token should be allowed ")
    void requestsToShortlinkWithoutJwtTokenShouldBeAllowed() {

        String requestBody = """
                {
                    "url": "https://example.com/some/long/path"
                }
                """;

        EntityExchangeResult<Map<String, Object>> data = webTestClient.post()
                .uri("/api/shortlinks")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TOKEN)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(new ParameterizedTypeReference<Map<String, Object>>() {
                }) // Deserialize the JSON into a Map
                .returnResult();

        Assertions.assertNotNull(data.getResponseBody());
        String shortCode = (String) data.getResponseBody().get("shortCode");

        webTestClient.get()
                .uri("/s/" + shortCode)
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "https://example.com/some/long/path");
    }
}
