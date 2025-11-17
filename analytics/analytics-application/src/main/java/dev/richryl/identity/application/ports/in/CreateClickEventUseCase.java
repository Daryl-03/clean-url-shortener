package dev.richryl.identity.application.ports.in;

import java.util.UUID;

public interface CreateClickEventUseCase {
    void handle(UUID shortlinkId);
}
