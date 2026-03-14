package dev.richryl.analytics.adapters.web;

import dev.richryl.analytics.application.ports.dto.*;
import dev.richryl.analytics.domain.DeviceInfo;
import dev.richryl.analytics.domain.GeoLocation;
import dev.richryl.analytics.application.ports.in.RetrieveClickEventsUseCase;
import dev.richryl.analytics.application.ports.in.RetrieveRangedClickEventsUseCase;
import dev.richryl.analytics.application.ports.in.RetrieveRangedCuratedClickEventsUseCase;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
                                                new ClickPerDayPerDeviceTypeStat(
                                                        Instant.now().minus(3, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS),
                                                        Map.of(
                                                                "desktop", 2,
                                                                "mobile", 1,
                                                                "others", 0
                                                        )
                                                ),
                                                new ClickPerDayPerDeviceTypeStat(
                                                        Instant.now().minus(2, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS),
                                                        Map.of(
                                                                "desktop", 0,
                                                                "mobile", 1,
                                                                "others", 0)
                                                ),
                                                new ClickPerDayPerDeviceTypeStat(
                                                        Instant.now().minus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS),
                                                        Map.of(
                                                                "desktop", 1,
                                                                "mobile", 0,
                                                                "others", 0)
                                                )
                                        )
                                )

                );

        mockMvc.perform(get("/api/analytics/{urlId}/curated", id)
                .param("from", Instant.now().minus(7, ChronoUnit.DAYS).toString())
                .param("to", Instant.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalClicks").value(4))
                .andExpect(jsonPath("$.browserStats[0].browser").value("Chrome"))
                .andExpect(jsonPath("$.countryStats[0].countryName").value("Germany"))
                .andExpect(jsonPath("$.clicksPerDayPerDeviceType[0].countsPerDeviceType").exists())
                .andExpect(jsonPath("$.clicksPerDayPerDeviceType[0].date").exists())
                .andExpect(jsonPath("$.clicksPerDayPerDeviceType[0].countsPerDeviceType.desktop").exists())
                .andExpect(jsonPath("$.clicksPerDayPerDeviceType[0].countsPerDeviceType.mobile").exists())
                .andExpect(jsonPath("$.clicksPerDayPerDeviceType[0].countsPerDeviceType.others").exists());

        verify(retrieveRangedCuratedClickEventsUseCase, times(1)).handle(any(UUID.class), any(Instant.class), any(Instant.class));
    }
}
