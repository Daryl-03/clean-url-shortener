package application.mocks;

import dev.richryl.shortlink.application.ports.out.ShortlinkIdGenerator;

import java.util.UUID;

public class FakeIdGenerator implements ShortlinkIdGenerator {
    private long currentId = 1;

    public synchronized long generateId() {
        return currentId++;
    }

    @Override
    public UUID generate() {
        return new UUID(0, generateId());
    }
}
