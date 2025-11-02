package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.Shortlink;

public interface GetShortlinkUseCase {
    Shortlink handle(String shortCode);
}
