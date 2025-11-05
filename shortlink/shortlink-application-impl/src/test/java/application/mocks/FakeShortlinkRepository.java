package application.mocks;

import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public void deleteByShortCode(String shortCode) {
        shortlinks.removeIf(shortlink -> shortlink.getShortCode().equals(shortCode));
    }

}
