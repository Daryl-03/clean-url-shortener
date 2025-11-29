package dev.richryl.analytics.application.ports.in;

import dev.richryl.analytics.application.ports.mocks.FakeClickEventRepository;
import dev.richryl.analytics.domain.ClickEvent;
import dev.richryl.analytics.domain.DeviceInfo;
import dev.richryl.analytics.domain.GeoLocation;
import dev.richryl.identity.application.ports.dto.ClickEventResponse;
import dev.richryl.identity.application.ports.in.RetrieveClickEventsUseCase;
import dev.richryl.identity.application.ports.out.ClickEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RetrieveClickEventsUseCaseTest {
    private RetrieveClickEventsUseCase retrieveClickEventsUseCase;
    private final UUID firstShortlinkId = UUID.randomUUID();
    private final UUID secondShortlinkId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        ClickEventRepository clickEventRepository = new FakeClickEventRepository(
                List.of(
                        new ClickEvent(
                                UUID.randomUUID(),
                                Instant.now(),
                                firstShortlinkId,
                                "referrer1",
                                GeoLocation.unknown(),
                                DeviceInfo.unknown()
                        ),
                        new ClickEvent(
                                UUID.randomUUID(),
                                Instant.now().plus(1, ChronoUnit.HOURS),
                                firstShortlinkId,
                                "referrer2",
                                GeoLocation.unknown(),
                                DeviceInfo.unknown()
                        ),
                        new ClickEvent(
                                UUID.randomUUID(),
                                Instant.now(),
                                secondShortlinkId,
                                "referrer3",
                                GeoLocation.unknown(),
                                DeviceInfo.unknown()
                        )
                )
        );
        retrieveClickEventsUseCase = new RetrieveClickEventsInteractor(clickEventRepository);
    }

    @Test
    @DisplayName("should retrieve click events for a given shortlink ID")
    void shouldRetrieveClickEventsForGivenShortlinkId() {

        List<ClickEventResponse> events = retrieveClickEventsUseCase.handle(firstShortlinkId);
        assertEquals(2, events.size());
    }
}
