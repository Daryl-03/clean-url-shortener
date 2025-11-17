package dev.richryl.analytics.adapters.persistence;

import dev.richryl.analytics.domain.ClickEvent;
import dev.richryl.identity.application.ports.out.ClickEventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InMemoryClickEventRepository implements ClickEventRepository {
    private final List<ClickEvent> clickEvents;

    public InMemoryClickEventRepository() {
        clickEvents = new ArrayList<>();
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
}
