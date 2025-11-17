package dev.richryl.shortlink.application.ports.out;

import java.util.UUID;

public interface AnalyticsPort {
    void recordShortlinkAccess(UUID shortlinkId);
}
