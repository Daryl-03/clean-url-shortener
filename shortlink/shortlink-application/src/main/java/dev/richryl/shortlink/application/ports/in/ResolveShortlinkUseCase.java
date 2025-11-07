package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.exceptions.ShortlinkNotFoundException;

public interface ResolveShortlinkUseCase {
    Shortlink handle(String shortCode) throws ShortlinkNotFoundException;
}
