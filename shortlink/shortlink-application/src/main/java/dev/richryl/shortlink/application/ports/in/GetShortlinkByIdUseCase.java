package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.exceptions.ShortlinkNotFoundException;

import java.util.UUID;

public interface GetShortlinkByIdUseCase {
    Shortlink handle(UUID shortlinkId) throws ShortlinkNotFoundException;
}
