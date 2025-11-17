package dev.richryl.shortlink.adapters.persistence;

import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class ShortlinkRepositoryTest {

    private ShortlinkRepository shortlinkRepository;

    @BeforeEach
    void setup() {
        shortlinkRepository = getShortlinkRepositoryImplementation();
    }

    abstract ShortlinkRepository getShortlinkRepositoryImplementation();

    @Test
    @DisplayName("Should save and find shortlink by short code")
    void testSaveAndFindByShortCode() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        Shortlink shortlink = saveAndGetShortlink(id, ownerId);
        Shortlink retrievedShortlink = shortlinkRepository.findByShortCode("exmpl").orElse(null);

        assertNotNull(retrievedShortlink);
        assertEquals(retrievedShortlink, shortlink);
    }

    @Test
    @DisplayName("Should save and find shortlink by id")
    void saveAndFindById() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        Shortlink shortlink = saveAndGetShortlink(id, ownerId);
        Shortlink retrievedShortlink = shortlinkRepository.findById(id).orElse(null);
        assertNotNull(retrievedShortlink);
        assertEquals(retrievedShortlink, shortlink);
    }

    @Test
    @DisplayName("Should update a shortlink given its id")
    void updateShortlink() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        Shortlink shortlink = saveAndGetShortlink(id, ownerId);

        Shortlink updatedShortlink = new Shortlink(
                id,
                "https://updatedexample.com",
                "exmpl",
                shortlink.getOwnerId(),
                shortlink.getCreatedAt(),
                shortlink.getUpdatedAt()
        );
        Shortlink result = shortlinkRepository.update(updatedShortlink).orElse(null);

        assertNotNull(result);

        Shortlink verif = shortlinkRepository.findById(id).orElse(null);
        assertNotNull(verif);
        assertEquals("https://updatedexample.com", verif.getOriginalUrl());
        assertEquals("exmpl", verif.getShortCode());
    }

    private Shortlink saveAndGetShortlink(UUID id, UUID ownerId) {
        Shortlink shortlink = new Shortlink(
                id,
                "https://example.com",
                "exmpl",
                ownerId,
                Instant.now(),
                Instant.now()
        );
        shortlinkRepository.save(shortlink);
        return shortlink;
    }

    @Test
    @DisplayName("Should find shortlink by owner id")
    void saveAndFindByOwnerId() {
        UUID id = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        Shortlink shortlink = saveAndGetShortlink(id, ownerId);

        var shortlinks = shortlinkRepository.findAllByOwnerId(ownerId);
        assertNotNull(shortlinks);
        assertEquals(1, shortlinks.size());
        assertEquals(shortlink.getId(), shortlinks.getFirst().id());
    }

}
