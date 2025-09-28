package dev.richryl.shortlink.application.ports.in;

import dev.richryl.shortlink.Shortlink;

public interface CreateShortlinkUseCase {

    Shortlink handle(String url);

}