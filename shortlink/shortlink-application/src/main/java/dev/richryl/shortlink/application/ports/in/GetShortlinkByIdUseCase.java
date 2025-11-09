package dev.richryl.shortlink.application.ports.in;


import dev.richryl.shortlink.application.exceptions.ShortlinkNotFoundException;
import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;

import java.util.UUID;

public interface GetShortlinkByIdUseCase {
    ShortlinkResponse handle(UUID shortlinkId) throws ShortlinkNotFoundException;
}
