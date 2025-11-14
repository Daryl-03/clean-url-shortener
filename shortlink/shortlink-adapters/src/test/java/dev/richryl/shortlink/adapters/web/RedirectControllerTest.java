package dev.richryl.shortlink.adapters.web;

import dev.richryl.bootstrap.ShortlinkApp;
import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;
import dev.richryl.shortlink.application.ports.in.ResolveShortlinkUseCase;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShortlinkController.class)
@ContextConfiguration(classes = {RedirectController.class, ShortlinkApp.class})
public class RedirectControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ResolveShortlinkUseCase resolveShortlinkUseCase;


    @Test
    @DisplayName("Redirect to original URL when requesting with short code")
    void redirectToOriginalUrlWhenRequestingWithShortCode() throws Exception {
        String originalUrl = "https://example.com/some/long/path";
        String shortcode = "abc123";

        when(resolveShortlinkUseCase.handle(anyString())
        ).thenReturn(new ShortlinkResponse(UUID.randomUUID(), originalUrl, shortcode));

        mockMvc.perform(get("/s/{shortcode}", shortcode)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", originalUrl));


        verify(resolveShortlinkUseCase, times(1)).handle(shortcode);
    }
}
