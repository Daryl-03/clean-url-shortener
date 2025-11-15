package dev.richryl.shortlink.application.ports.out;

import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShortlinkRepository {

    void save(Shortlink shortlink);
    Optional<Shortlink> findByShortCode(String shortCode);
    Optional<Shortlink> findById(UUID id);
    void deleteById(UUID id);

    Optional<Shortlink> update(Shortlink updatedShortlink);

    List<ShortlinkResponse> findAllByOwnerId(UUID userId);
}
