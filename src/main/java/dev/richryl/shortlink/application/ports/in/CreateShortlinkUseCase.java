package dev.richryl.shortlink.shortlink_application.ports.in;

import dev.richryl.shortlink.shortlink_domain.Shortlink;

public interface CreateShortlinkUseCase {
    Shortlink handle(String url);
}
