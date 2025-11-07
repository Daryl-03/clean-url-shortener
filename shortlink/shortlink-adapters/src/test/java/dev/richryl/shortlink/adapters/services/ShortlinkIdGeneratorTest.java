package dev.richryl.shortlink.adapters.services;

import dev.richryl.shortlink.application.ports.out.ShortlinkIdGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ShortlinkIdGeneratorTest {

    @Test
    @DisplayName("Should generate IDs")
    void should_generate_ids() {
        ShortlinkIdGenerator idGenerator = new UuidIdGenerator();
        UUID id1 = idGenerator.generate();
        assertNotNull(id1);
    }
}
