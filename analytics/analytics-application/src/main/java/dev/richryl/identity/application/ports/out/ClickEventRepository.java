package dev.richryl.identity.application.ports.out;

import dev.richryl.analytics.domain.ClickEvent;

import java.util.List;
import java.util.UUID;

public interface ClickEventRepository {

    List<ClickEvent> findByShortlinkId(UUID shortlinkId);

    void save(ClickEvent clickEvent);
}
