package dev.richryl.shortlink.adapters.web;

import dev.richryl.bootstrap.ShortlinkApp;
import dev.richryl.identity.application.ports.dto.CreateClickEventCommand;
import dev.richryl.identity.application.ports.in.CreateClickEventUseCase;

import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;
import dev.richryl.shortlink.application.ports.in.ResolveShortlinkUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

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
    @MockitoBean
    private CreateClickEventUseCase createClickEventUseCase;


    @Test
    @DisplayName("Redirect to original URL when requesting with short code")
    void redirectToOriginalUrlWhenRequestingWithShortCode() throws Exception {
        String originalUrl = "https://example.com/some/long/path";
        String shortcode = "abc123";
        UUID uuid = UUID.randomUUID();

        CreateClickEventCommand clickEventCommand = new CreateClickEventCommand(
                uuid,
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.3",
                "210.215.214.142",
                "https://referrer.com/page",
                "fr-FR"
        );

        when(resolveShortlinkUseCase.handle(shortcode)
        ).thenReturn(new ShortlinkResponse(uuid, originalUrl, shortcode, Instant.now(), Instant.now()));
        doNothing().when(createClickEventUseCase).handle(any(CreateClickEventCommand.class));

        mockMvc.perform(get("/s/{shortcode}", shortcode)
                        .contentType(APPLICATION_JSON)
                        .header("User-Agent", clickEventCommand.userAgent())
                        .header("X-Forwarded-For", clickEventCommand.ipAddress())
                        .header("Referer", clickEventCommand.referrer())
                        .header("Accept-Language", clickEventCommand.acceptLanguage())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", originalUrl));


        verify(resolveShortlinkUseCase, times(1)).handle(shortcode);
        verify(createClickEventUseCase, times(1)).handle(clickEventCommand);
    }
}
