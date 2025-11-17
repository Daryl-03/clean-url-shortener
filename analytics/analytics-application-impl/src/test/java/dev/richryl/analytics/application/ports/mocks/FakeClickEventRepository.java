package dev.richryl.analytics.application.ports.mocks;

import dev.richryl.analytics.domain.ClickEvent;
import dev.richryl.identity.application.ports.out.ClickEventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FakeClickEventRepository implements ClickEventRepository {
    private List<ClickEvent> clickEvents;

    public FakeClickEventRepository(List<ClickEvent> clickEvents) {
        this.clickEvents = clickEvents;
    }

    public FakeClickEventRepository() {
        clickEvents = new ArrayList<>();
    }

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
