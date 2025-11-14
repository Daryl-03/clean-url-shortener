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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
                .header("Authorization", "Bearer invalid")
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isUnauthorized();

        webTestClient.post()
                .uri("/api/shortlinks")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isForbidden();
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

    @Test
    @DisplayName("Should return user identity information when requested with valid jwt token")
    void shouldReturnUserIdentityInformationWhenRequestedWithValidJwtToken() {
        webTestClient.get()
                .uri("/api/identity/me")
                .header("Authorization", "Bearer " + TOKEN)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.userId").isEqualTo("123e4567-e89b-12d3-a456-426614174000")
                .jsonPath("$.username").isEqualTo("testuser")
                .jsonPath("$.email").exists()
                .jsonPath("$.firstName").exists()
                .jsonPath("$.lastName").exists();
    }

}
