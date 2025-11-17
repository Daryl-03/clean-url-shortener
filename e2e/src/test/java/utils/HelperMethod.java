package utils;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;

public class HelperMethod {
    private static final String TOKEN = AppConstants.JWT_TOKEN;

    public record ResponseData(EntityExchangeResult<Map<String, Object>> data) {
    }

    public static ResponseData createShortlinkRequest(String requestBody, WebTestClient webTestClient) {
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
        return new ResponseData(data);
    }
}
