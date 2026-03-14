package dev.richryl.shortlink.adapters.web;

import dev.richryl.shortlink.application.exceptions.ShortlinkNotFoundException;
import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;
import dev.richryl.shortlink.application.ports.dto.UpdateShortlinkCommand;
import dev.richryl.shortlink.application.ports.in.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(
        controllers = ShortlinkController.class
)
@ContextConfiguration(classes = {ShortlinkController.class, GlobalShortlinkExceptionHandler.class})
@WithMockUser(value = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
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
    @MockitoBean
    private RetrieveAllShortlinksForUserUseCase retrieveAllShortlinksForUserUseCase;
    @MockitoBean
    private GetShortlinkByShortcodeUseCase getShortlinkByShortcodeUseCase;


    private final UUID userId = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");

    @BeforeEach
    public void setUp() {

        when(createShortlinkUseCase.handle(anyString(), eq(userId)))
                .thenAnswer(invocation -> {
                    String url = invocation.getArgument(0);
                    return new ShortlinkResponse(UUID.randomUUID(), url, "abc123", Instant.now(), Instant.now());
                });
    }

    @Test
    @DisplayName("When POST request to create shortlink is made, then returns created shortlink")
    public void whenPostRequestToCreateShortlink_thenReturnsCreatedShortlink() throws Exception {
        String originalUrl = "https://example.com/some/long/path";

        String requestBody = String.format("""
                {
                    "url": "%s"
                }
                """, originalUrl);

        mockMvc.perform(post("/api/shortlinks")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf())
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.originalUrl").value(originalUrl))
                .andExpect(jsonPath("$.shortCode").value("abc123"));

        verify(createShortlinkUseCase, times(1)).handle(anyString(), eq(userId));
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
                        .content(requestBody)
                        .with(csrf())
                )
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
                        .with(csrf())
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
        ).thenReturn(new ShortlinkResponse(id, originalUrl, shortcode, Instant.now(), Instant.now()));

        mockMvc.perform(get("/api/shortlinks/{id}", id)
                        .contentType(APPLICATION_JSON)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalUrl").value(originalUrl))
                .andExpect(jsonPath("$.shortCode").value(shortcode))
                .andExpect(jsonPath("$.id").value(id.toString()));

        verify(getShortlinkByIdUseCase, times(1)).handle(id);
    }

    @Test
    @DisplayName("Retrieve shortlink by shortcode when it exists")
    void returnShortlinkByShortcodeWhenExists() throws Exception {
        String originalUrl = "https://example.com/some/long/path";
        String shortcode = "abc123";
        UUID id = UUID.randomUUID();

        when(getShortlinkByShortcodeUseCase.handle(shortcode)
        ).thenReturn(new ShortlinkResponse(id, originalUrl, shortcode, Instant.now(), Instant.now()));

        mockMvc.perform(get("/api/shortlinks/slug/{shortcode}", shortcode)
                        .contentType(APPLICATION_JSON)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.originalUrl").value(originalUrl))
                .andExpect(jsonPath("$.shortCode").value(shortcode))
                .andExpect(jsonPath("$.id").value(id.toString()));

        verify(getShortlinkByShortcodeUseCase, times(1)).handle(shortcode);
    }

    @Test
    @DisplayName("Delete shortlink when it exists")
    void deleteShortlinkWhenExists() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/shortlinks/{id}", id)
                        .contentType(APPLICATION_JSON)
                        .with(csrf())
                )
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
        ShortlinkResponse object = new ShortlinkResponse(id, originalUrl, shortcode, Instant.now(), Instant.now());
        UpdateShortlinkCommand command = new UpdateShortlinkCommand(id, originalUrl);

        when(updateShortlinkByIdUseCase.handle(command)).thenReturn(object);

        mockMvc.perform(
                        put("/api/shortlinks").contentType(APPLICATION_JSON)
                                .content(requestBody)
                                .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(object.id().toString()))
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
                                .with(csrf())
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.status").exists());
    }

    @Test
    @DisplayName("should return a list of shortlinks for the authenticated user")
    void  shouldReturnListOfShortlinksForAuthenticatedUser() throws Exception {
        UUID shortlinkId1 = UUID.randomUUID();
        UUID shortlinkId2 = UUID.randomUUID();

        when(retrieveAllShortlinksForUserUseCase.handle(userId))
                .thenReturn(java.util.List.of(
                        new ShortlinkResponse(shortlinkId1, "https://example1.com", "code1", Instant.now(), Instant.now()),
                        new ShortlinkResponse(shortlinkId2, "https://example2.com", "code2", Instant.now(), Instant.now())
                ));

        mockMvc.perform(get("/api/shortlinks")
                        .contentType(APPLICATION_JSON)
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(shortlinkId1.toString()))
                .andExpect(jsonPath("$.[1].id").value(shortlinkId2.toString()));

        verify(retrieveAllShortlinksForUserUseCase, times(1)).handle(userId);
    }
}