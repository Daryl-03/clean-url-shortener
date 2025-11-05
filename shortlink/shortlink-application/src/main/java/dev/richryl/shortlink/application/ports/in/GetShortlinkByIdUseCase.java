package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.Shortlink;

import java.util.UUID;

public interface GetShortlinkByIdUseCase {
    Shortlink handle(UUID shortlinkId);
}
