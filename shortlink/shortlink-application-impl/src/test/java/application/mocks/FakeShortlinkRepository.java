package application.mocks;

import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FakeShortlinkRepository implements ShortlinkRepository {

    private final List<Shortlink> shortlinks;

    public FakeShortlinkRepository() {
        shortlinks = new ArrayList<>();
    }

    public FakeShortlinkRepository(List<Shortlink> shortlinks) {
        this.shortlinks = shortlinks;
    }

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
    public Optional<Shortlink> findById(UUID id) {
        return shortlinks.stream()
                .filter(shortlink -> shortlink.getId().equals(id))
                .findFirst();
    }

    @Override
    public void deleteById(UUID id) {
        shortlinks.removeIf(shortlink -> shortlink.getId().equals(id));
    }

    @Override
    public Optional<Shortlink> update(Shortlink updatedShortlink) {
        return shortlinks.stream()
                .filter(shortlink -> shortlink.getId().equals(updatedShortlink.getId()))
                .findFirst()
                .map(existingShortlink -> {
                    shortlinks.remove(existingShortlink);
                    shortlinks.add(updatedShortlink);
                    return updatedShortlink;
                });
    }

    @Override
    public List<ShortlinkResponse> findAllByOwnerId(UUID userId) {
        return shortlinks.stream()
                .filter(shortlink -> shortlink.getOwnerId().equals(userId))
                .map(ShortlinkResponse::fromDomain)
                .toList();
    }

}
