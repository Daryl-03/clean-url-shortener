package dev.richryl.shortlink.adapters.persistence;

import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryShortlinkRepository implements ShortlinkRepository {

        private final List<Shortlink> shortlinks = new ArrayList<>();

    @Override
    public void save(Shortlink shortlink) {
        shortlinks.add(shortlink);
    }

    @Override
    public Optional<Shortlink> findByShortCode(String shortCode) {
        return shortlinks.stream()
                .filter(shortlink -> shortlink.getShortCode().equals(shortCode))
                .findFirst();
    }

    @Override
    public void deleteByShortCode(String shortCode) {
        shortlinks.removeIf(shortlink -> shortlink.getShortCode().equals(shortCode));
    }
}
