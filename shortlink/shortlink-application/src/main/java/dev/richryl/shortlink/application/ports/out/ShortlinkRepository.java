package dev.richryl.shortlink.application.ports.out;

import dev.richryl.shortlink.Shortlink;

import java.util.Optional;

public interface ShortlinkRepository {

    void save(Shortlink shortlink);
    Optional<Shortlink> findByShortCode(String shortCode);
}
