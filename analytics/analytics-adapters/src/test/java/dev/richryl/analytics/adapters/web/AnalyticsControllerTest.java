package dev.richryl.analytics.adapters.web;

import dev.richryl.analytics.domain.DeviceInfo;
import dev.richryl.analytics.domain.GeoLocation;
import dev.richryl.identity.application.ports.dto.ClickEventResponse;
import dev.richryl.identity.application.ports.in.RetrieveClickEventsUseCase;
import dev.richryl.identity.application.ports.in.RetrieveRangedClickEventsUseCase;
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

import static org.mockito.ArgumentMatchers.any;
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
    @MockitoBean
    private RetrieveRangedClickEventsUseCase retrieveRangedClickEventsUseCase;

    @Test
    @DisplayName("Should return a list of click events related to a shortened URL")
    void shouldReturnListOfClickEventsBasedOnId() throws Exception {
        UUID id = UUID.randomUUID();
        when(retrieveClickEventsUseCase.handle(id))
                .thenReturn(
                        List.of(
                                new ClickEventResponse(
                                        UUID.randomUUID(),
                                        Instant.now(),
                                        "US",
                                        GeoLocation.unknown(),
                                        DeviceInfo.unknown()
                                ),
                                new ClickEventResponse(
                                        UUID.randomUUID(),
                                        Instant.now().plus(1, ChronoUnit.HOURS),
                                        "FR",
                                        new GeoLocation("France", "Ile-de-France", "Paris"),
                                        new DeviceInfo("Chrome", "Windows 10", "Desktop"
                                        )
                                )
                        ));

        mockMvc.perform(get("/api/analytics/{urlId}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].timestamp").exists());
    }

    @Test
    @DisplayName("Should return a list of click events related to a shortened URL within a date range")
    void shouldReturnListOfClickEventsBasedOnIdWithinDateRange() throws Exception {
        UUID id = UUID.randomUUID();
        when(retrieveRangedClickEventsUseCase.handle(any(UUID.class), any(Instant.class), any(Instant.class)))
                .thenReturn(
                        List.of(
                                new ClickEventResponse(
                                        UUID.randomUUID(),
                                        Instant.now(),
                                        "US",
                                        GeoLocation.unknown(),
                                        DeviceInfo.unknown()
                                ),
                                new ClickEventResponse(
                                        UUID.randomUUID(),
                                        Instant.now().plus(1, ChronoUnit.HOURS),
                                        "FR",
                                        new GeoLocation("France", "Ile-de-France", "Paris"),
                                        new DeviceInfo("Chrome", "Windows 10", "Desktop"
                                        )
                                )
                        ));

        mockMvc.perform(get("/api/analytics/{urlId}/ranged", id)
                .param("from", Instant.now().minus(7, ChronoUnit.DAYS).toString())
                .param("to", Instant.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[1].location.countryName").value("Ile-de-France"));
    }
}
