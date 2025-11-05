package dev.richryl.shortlink.application.ports.in;

import java.util.UUID;

public interface DeleteShortlinkByIdUseCase {
    void handle(UUID id);
}
