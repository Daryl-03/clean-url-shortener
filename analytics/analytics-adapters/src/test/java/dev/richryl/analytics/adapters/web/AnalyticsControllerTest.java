package dev.richryl.analytics.adapters.web;

import dev.richryl.analytics.domain.DeviceInfo;
import dev.richryl.analytics.domain.GeoLocation;
import dev.richryl.identity.application.ports.dto.ClickEventResponse;
import dev.richryl.identity.application.ports.in.RetrieveClickEventsUseCase;
import dev.richryl.identity.application.ports.in.RetrieveRangedClickEventsUseCase;
import dev.richryl.identity.application.ports.in.RetrieveRangedCuratedClickEventsUseCase;
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
import java.util.ArrayList;
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
    @MockitoBean
    private RetrieveRangedCuratedClickEventsUseCase retrieveRangedCuratedClickEventsUseCase;

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

    @Test
    @DisplayName("Should return a curated list of click events related to a shortened URL within a date range")
    void shouldReturnCuratedListOfClickEventsBasedOnIdWithinDateRange() throws Exception {
        UUID id = UUID.randomUUID();
        when(retrieveRangedCuratedClickEventsUseCase.handle(any(UUID.class), any(Instant.class), any(Instant.class)))
                .thenReturn(
                                new ClickEventStatResponse(
                                        4,
                                        List.of(
                                                new BrowserStat("Chrome", 2),
                                                new BrowserStat("Firefox", 1),
                                                new BrowserStat("Safari", 1)
                                        ),
                                        List.of(
                                                new CountryStat("Germany", "Berlin", 3),
                                                new CountryStat("France", "paris", 1)
                                        ),
                                        List.of(
                                                new ClickPerDayDeviceTypeStat(
                                                        Instant.now().minus(3, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS),
                                                        "Desktop",
                                                        2
                                                ),
                                                new ClickPerDayDeviceTypeStat(
                                                        Instant.now().minus(2, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS),
                                                        "Mobile",
                                                        1
                                                ),
                                                new ClickPerDayDeviceTypeStat(
                                                        Instant.now().minus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS),
                                                        "Tablet",
                                                        1
                                                )
                                        )
                                )

                );

    }
}
