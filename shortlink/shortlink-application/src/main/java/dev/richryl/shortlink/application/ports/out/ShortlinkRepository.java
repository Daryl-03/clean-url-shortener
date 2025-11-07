package dev.richryl.shortlink.application.ports.out;

import dev.richryl.shortlink.Shortlink;

import java.util.Optional;
import java.util.UUID;

public interface ShortlinkRepository {

    void save(Shortlink shortlink);
    Optional<Shortlink> findByShortCode(String shortCode);
    Optional<Shortlink> findById(UUID id);
    void deleteById(UUID id);

    Shortlink update(Shortlink updatedShortlink);
}
