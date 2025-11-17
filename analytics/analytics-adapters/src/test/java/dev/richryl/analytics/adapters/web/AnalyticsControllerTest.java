package dev.richryl.analytics.adapters.web;

import dev.richryl.identity.application.ports.dto.ClickEventResponse;
import dev.richryl.identity.application.ports.in.RetrieveClickEventsUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AnalyticsController.class)
@ContextConfiguration(classes = AnalyticsController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RetrieveClickEventsUseCase retrieveClickEventsUseCase;

    @Test
    @DisplayName("Should return a list of click events related to a shortened URL")
    void shouldReturnListOfClickEventsBasedOnId() throws Exception {
        UUID id = UUID.randomUUID();
        when(retrieveClickEventsUseCase.handle(id))
                .thenReturn(
                        List.of(
                                new ClickEventResponse(
                                        UUID.randomUUID(),
                                        Instant.now()
                                ),
                                new ClickEventResponse(
                                        UUID.randomUUID(),
                                        Instant.now().plus(1, ChronoUnit.HOURS)
                                )
                        )
                );

        mockMvc.perform(get("/api/analytics/{urlId}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].timestamp").exists());
    }
}
