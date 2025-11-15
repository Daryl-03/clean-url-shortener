package dev.richryl.shortlink.application.ports.in;


import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;

import java.util.UUID;

public interface CreateShortlinkUseCase {

    ShortlinkResponse handle(String url, UUID ownerId);

}