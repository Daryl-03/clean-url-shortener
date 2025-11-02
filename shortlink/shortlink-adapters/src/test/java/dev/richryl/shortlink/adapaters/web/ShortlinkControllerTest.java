package dev.richryl.shortlink.adapaters.web;

import dev.richryl.bootstrap.ShortlinkApp;
import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.ports.in.CreateShortlinkUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.originalUrl").value(originalUrl));

        verify(createShortlinkUseCase, times(1)).handle(anyString());
    }
}