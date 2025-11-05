package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.Shortlink;

public interface GetShortlinkByShortcodeUseCase {
    Shortlink handle(String shortCode);
}
