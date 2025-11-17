package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;

import java.util.List;
import java.util.UUID;

public interface RetrieveAllShortlinksForUserUseCase {
    List<ShortlinkResponse> handle(UUID userId);
}
