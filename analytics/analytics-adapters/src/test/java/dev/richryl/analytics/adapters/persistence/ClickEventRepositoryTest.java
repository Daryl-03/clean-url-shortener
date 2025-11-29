package dev.richryl.analytics.adapters.persistence;

import dev.richryl.analytics.domain.ClickEvent;
import dev.richryl.analytics.domain.DeviceInfo;
import dev.richryl.analytics.domain.GeoLocation;
import dev.richryl.identity.application.ports.out.ClickEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class ClickEventRepositoryTest {
    private ClickEventRepository clickEventRepository;

    protected abstract ClickEventRepository getClickEventRepository();

    @BeforeEach
    void setUp() {
        clickEventRepository = getClickEventRepository();
    }

    @Test
    void testSaveAndFindByShortlinkId() {
        UUID clickEventId = UUID.randomUUID();
        UUID urlId = UUID.randomUUID();
        ClickEvent clickEvent = new ClickEvent(
                clickEventId,
                java.time.Instant.now(),
                urlId,
                "http://example.com",
                GeoLocation.unknown(),
                DeviceInfo.unknown()
        );

        clickEventRepository.save(clickEvent);

        ClickEvent retrievedEvent = clickEventRepository.findByShortlinkId(urlId).getFirst();
        assertEquals(clickEventId, retrievedEvent.getId());
        assertEquals(urlId, retrievedEvent.getShortlinkId());
    }
}
