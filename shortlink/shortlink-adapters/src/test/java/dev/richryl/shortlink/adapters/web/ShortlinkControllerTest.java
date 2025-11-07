package dev.richryl.shortlink.adapters.web;

import dev.richryl.bootstrap.ShortlinkApp;
import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.exceptions.ShortlinkNotFoundException;
import dev.richryl.shortlink.application.ports.dto.UpdateShortlinkCommand;
import dev.richryl.shortlink.application.ports.in.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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
    @MockitoBean
    private UpdateShortlinkByIdUseCase updateShortlinkByIdUseCase;

    @BeforeEach
    public void setUp() {

        when(createShortlinkUseCase.handle(anyString()))
                .thenAnswer(invocation -> {
                    String url = invocation.getArgument(0);
                    return new Shortlink(UUID.randomUUID(), url, "abc123");
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
        ).thenReturn(new Shortlink(id, originalUrl, shortcode));

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

    @Test
    @DisplayName("Update shortlink when it exists")
    void updateShortlinkWhenExists() throws Exception {
        String originalUrl = "https://example.com/some/long/path";
        String shortcode = "abc123";
        UUID id = UUID.randomUUID();

        String requestBody = String.format("""
                {
                    "id": "%s",
                    "url": "%s"
                }
                """, id, originalUrl);
        Shortlink object = new Shortlink(id, originalUrl, shortcode);
        UpdateShortlinkCommand command = new UpdateShortlinkCommand(id, originalUrl);

        when(updateShortlinkByIdUseCase.handle(command)).thenReturn(object);

        mockMvc.perform(
                        put("/api/shortlinks").contentType(APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(object.getId().toString()))
                .andExpect(jsonPath("$.originalUrl").value(originalUrl));

        verify(updateShortlinkByIdUseCase, times(1)).handle(command);
    }

    @Test
    @DisplayName("When updating non existing shortlinkn, return 404 Not Found")
    void returnNotFoundWhenUpdatingNonExistingShortlink() throws Exception {
        UUID nonExistingId = UUID.randomUUID();
        String originalUrl = "https://example.com/some/long/path";
        String requestBody = String.format("""
                {
                    "id": "%s",
                    "url": "%s"
                }
                """, nonExistingId, originalUrl);
        when(updateShortlinkByIdUseCase.handle(any(UpdateShortlinkCommand.class))
        ).thenThrow(new ShortlinkNotFoundException("Shortlink not found"));

        mockMvc.perform(
                        put("/api/shortlinks").contentType(APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.status").exists());
    }



}