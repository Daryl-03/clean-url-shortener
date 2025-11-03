package dev.richryl.shortlink.adapters.persistence;

import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;

public class InMemoryShortlinkRepositoryTest extends ShortlinkRepositoryTest {

    @Override
    ShortlinkRepository getShortlinkRepositoryImplementation() {
        return new InMemoryShortlinkRepository();
    }
}
