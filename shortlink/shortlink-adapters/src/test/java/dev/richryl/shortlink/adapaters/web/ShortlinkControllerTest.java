package dev.richryl.shortlink.adapaters.web;

import dev.richryl.bootstrap.ShortlinkApp;
import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.ports.in.CreateShortlinkUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.mockito.ArgumentMatchers.anyString;


@WebMvcTest(ShortlinkController.class)
@ContextConfiguration(classes = {ShortlinkController.class, ShortlinkApp.class})
public class ShortlinkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateShortlinkUseCase createShortlinkUseCase;

    @BeforeEach
    public void setUp() {

        when(createShortlinkUseCase.handle(anyString()))
                .thenAnswer(invocation -> {
                    String url = invocation.getArgument(0);
                    return new Shortlink(url, "abc123");
                });
    }

    @Test
    public void whenPostRequestToCreateShortlink_thenReturnsCreatedShortlink() throws Exception {
        String originalUrl = "https://example.com/some/long/path";
        String shortCode = "abc123";

        String requestBody = String.format("""
                {
                    "url": "%s"
                }
                """, originalUrl);

        mockMvc.perform(post("/api/shortlinks")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.originalUrl").value(originalUrl));

        verify(createShortlinkUseCase, times(1)).handle(anyString());
    }

    @Test
    @DisplayName("When an invalid url is provided, return 400 Bad Request with invalid URL error message")
    void returnBadRequestForInvalidUrl() throws Exception {
        String requestBody = """
                {
                    "url": "invalid-url"
                }
                """;
        mockMvc.perform(post("/api/shortlinks")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.validationErrors.[0].field").value("url"));
    }

    @Test
    @DisplayName("When the request body is missing in the request, return 400 Bad Request with adequate error message")
    void returnBadRequestForMissingUrl() throws Exception {

        mockMvc.perform(post("/api/shortlinks")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.code").value("MISSING_REQUEST_BODY"))
                .andExpect(jsonPath("$.status").exists());

    }
}