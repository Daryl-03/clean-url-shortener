package dev.richryl.shortlink.adapters.web;

import dev.richryl.bootstrap.ShortlinkApp;
import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.ports.in.CreateShortlinkUseCase;
import dev.richryl.shortlink.application.ports.in.DeleteShortlinkByIdUseCase;
import dev.richryl.shortlink.application.ports.in.GetShortlinkByIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @MockitoBean
    private GetShortlinkByIdUseCase getShortlinkByIdUseCase;
    @MockitoBean
    private DeleteShortlinkByIdUseCase deleteShortlinkByIdUseCase;

    @BeforeEach
    public void setUp() {

        when(createShortlinkUseCase.handle(anyString()))
                .thenAnswer(invocation -> {
                    String url = invocation.getArgument(0);
                    return new Shortlink(UUID.randomUUID() ,url, "abc123");
                });
    }

    @Test
    public void whenPostRequestToCreateShortlink_thenReturnsCreatedShortlink() throws Exception {
        String originalUrl = "https://example.com/some/long/path";

        String requestBody = String.format("""
                {
                    "url": "%s"
                }
                """, originalUrl);

        mockMvc.perform(post("/api/shortlinks")
                .contentType(APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.originalUrl").value(originalUrl))
        .andExpect(jsonPath("$.shortCode").value("abc123"));

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
                        .contentType(APPLICATION_JSON)
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
                        .contentType(APPLICATION_JSON)
                        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.code").value("MISSING_REQUEST_BODY"))
                .andExpect(jsonPath("$.status").exists());

    }

    @Test
    @DisplayName("Retrieve shortlink when it exists")
    void returnShortlinkWhenExists() throws Exception {
        String originalUrl = "https://example.com/some/long/path";
        String shortcode = "abc123";
        UUID id = UUID.randomUUID();

        when(getShortlinkByIdUseCase.handle(any(UUID.class))
        ).thenReturn(new Shortlink(id , originalUrl, shortcode));

        mockMvc.perform(get("/api/shortlinks/{id}", id)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalUrl").value(originalUrl))
                .andExpect(jsonPath("$.shortCode").value(shortcode))
                .andExpect(jsonPath("$.id").value(id.toString()));

        verify(getShortlinkByIdUseCase, times(1)).handle(id);
    }

    @Test
    @DisplayName("Delete shortlink when it exists")
    void deleteShortlinkWhenExists() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/shortlinks/{id}", id)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(deleteShortlinkByIdUseCase, times(1)).handle(id);
    }

}