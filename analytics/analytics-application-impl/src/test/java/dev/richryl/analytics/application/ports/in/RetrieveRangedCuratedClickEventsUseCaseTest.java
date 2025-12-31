package dev.richryl.analytics.application.ports.in;

import dev.richryl.analytics.application.ports.dto.BrowserStat;
import dev.richryl.analytics.application.ports.dto.ClickPerDayPerDeviceTypeStat;
import dev.richryl.analytics.application.ports.dto.CountryStat;
import dev.richryl.analytics.application.ports.out.ClickEventStatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RetrieveRangedCuratedClickEventsUseCaseTest {
    private RetrieveRangedCuratedClickEventsUseCase useCase;
    private ClickEventStatRepository clickEventStatRepository;

    @BeforeEach
    void setup() {
        clickEventStatRepository = Mockito.mock(ClickEventStatRepository.class);
        useCase = new RetrieveRangedCuratedClickEventsInteractor(clickEventStatRepository);
    }

    @Test
    @DisplayName("should retrieve curated click events within a specified range")
    void testRetrieveRangedCuratedClickEvents() {
        var shortlinkId = UUID.randomUUID();
        var from = java.time.Instant.now().minusSeconds(3600);
        var to = java.time.Instant.now();

        when(clickEventStatRepository.retrieveBrowserStats(shortlinkId, from, to)).thenReturn(
                List.of(
                        new BrowserStat("Chrome", 150),
                        new BrowserStat("Firefox", 75)
                )
        );

        when(clickEventStatRepository.retrieveCountryStats(shortlinkId, from, to)).thenReturn(
                List.of(
                        new CountryStat("France", "Paris", 3),
                        new CountryStat("Russia", "Siberia", 4)
                )
        );

        when(clickEventStatRepository.retrieveClicksPerDayDeviceType(shortlinkId, from, to)).thenReturn(
                List.of(
                        new ClickPerDayPerDeviceTypeStat(Instant.now(),
                                Map.of(
                                        "desktop", 5,
                                        "mobile", 2,
                                        "other", 1
                                )),
                        new ClickPerDayPerDeviceTypeStat(Instant.now(),
                                Map.of(
                                        "desktop", 10,
                                        "mobile", 8,
                                        "other", 4
                                ))
                )
        );

        when(clickEventStatRepository.getNumberOfClicksInPeriod(shortlinkId, from, to)).thenReturn(
                40
        );

        var result = useCase.handle(shortlinkId, from, to);

        assertNotNull(result);
        assertEquals(40, result.totalClicks());
        assertEquals(2, result.clicksPerDayPerDeviceType().size());
        assertEquals(3, result.clicksPerDayPerDeviceType().getFirst().countsPerDeviceType().size());
        assertEquals(2, result.browserStats().size());
        assertEquals(2, result.countryStats().size());

        verify(clickEventStatRepository, times(1)).retrieveBrowserStats(any(UUID.class), any(Instant.class), any(Instant.class));
        verify(clickEventStatRepository, times(1)).retrieveCountryStats(any(UUID.class), any(Instant.class), any(Instant.class));
        verify(clickEventStatRepository, times(1)).retrieveClicksPerDayDeviceType(any(UUID.class), any(Instant.class), any(Instant.class));
        verify(clickEventStatRepository, times(1)).getNumberOfClicksInPeriod(any(UUID.class), any(Instant.class), any(Instant.class));
    }
}
