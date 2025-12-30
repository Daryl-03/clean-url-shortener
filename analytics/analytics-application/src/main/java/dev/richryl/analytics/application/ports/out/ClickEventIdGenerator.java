package dev.richryl.analytics.application.ports.out;

import java.util.UUID;

public interface ClickEventIdGenerator {
    UUID generate();
}
