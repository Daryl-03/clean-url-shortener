package dev.richryl.analytics.adapters.persistence;

import dev.richryl.analytics.domain.ClickEvent;
import dev.richryl.identity.application.ports.out.ClickEventRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryClickEventRepository implements ClickEventRepository {
    private final List<ClickEvent> clickEvents;

    public InMemoryClickEventRepository() {
        clickEvents = new CopyOnWriteArrayList<>();
    }

    @Override
    public List<ClickEvent> findByShortlinkId(UUID shortlinkId) {
        return clickEvents.stream()
                .filter(event -> event.getShortlinkId().equals(shortlinkId))
                .toList();
    }

    @Override
    public void save(ClickEvent clickEvent) {
        clickEvents.add(clickEvent);
    }

    @Override
    public List<ClickEvent> findByTimestampBetween(UUID shortlinkId, Instant start, Instant end) {
        return clickEvents.stream()
                .filter(event -> event.getShortlinkId().equals(shortlinkId))
                .filter(event -> !event.getTimestamp().isBefore(start) && !event.getTimestamp().isAfter(end))
                .toList();
    }
}
