package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;
import dev.richryl.shortlink.application.ports.dto.UpdateShortlinkCommand;

public interface UpdateShortlinkByIdUseCase {
    ShortlinkResponse handle(UpdateShortlinkCommand command);
}
