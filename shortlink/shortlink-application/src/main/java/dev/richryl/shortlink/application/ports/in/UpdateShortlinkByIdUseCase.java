package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.ports.dto.UpdateShortlinkCommand;

public interface UpdateShortlinkByIdUseCase {
    Shortlink handle(UpdateShortlinkCommand shortlink);
}
