package dev.richryl.analytics.application.ports.in;

import dev.richryl.analytics.domain.ClickEvent;
import dev.richryl.analytics.domain.DeviceInfo;
import dev.richryl.analytics.domain.GeoLocation;
import dev.richryl.identity.application.ports.dto.ClickEventResponse;
import dev.richryl.identity.application.ports.in.RetrieveRangedClickEventsUseCase;
import dev.richryl.identity.application.ports.out.ClickEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RetrieveRangedClickEventsUseCaseTest {
    private RetrieveRangedClickEventsUseCase retrieveRangedClickEventsUseCase;
    private ClickEventRepository clickEventRepository;

    @BeforeEach
    void setup() {
        clickEventRepository = Mockito.mock(ClickEventRepository.class);
        retrieveRangedClickEventsUseCase = new RetrieveRangedClickEventsInteractor(clickEventRepository);
    }


    @Test
    @DisplayName("Should return click events within date range")
    void shouldReturnClickEventsWithinDateRange() {
        Instant start = Instant.now().minus(4, ChronoUnit.DAYS);
        Instant end = Instant.now();
        ClickEvent clickEvent = new ClickEvent(
                UUID.randomUUID(),
                start.plus(1, ChronoUnit.DAYS),
                UUID.randomUUID(),
                "referer",
                GeoLocation.unknown(),
                DeviceInfo.unknown()
        );
        List<ClickEvent> expectedEvents = Collections.singletonList(clickEvent);

        when(clickEventRepository.findByTimestampBetween(any(UUID.class), eq(start), eq(end))).thenReturn(expectedEvents);


        List<ClickEventResponse> actualEvents = retrieveRangedClickEventsUseCase.handle(UUID.randomUUID(), start, end);

        assertEquals(1, actualEvents.size());
        assertEquals(clickEvent.getId(), actualEvents.getFirst().id());
        assertEquals(clickEvent.getTimestamp(), actualEvents.getFirst().timestamp());
        verify(clickEventRepository).findByTimestampBetween(any(UUID.class), eq(start), eq(end));
    }

}
