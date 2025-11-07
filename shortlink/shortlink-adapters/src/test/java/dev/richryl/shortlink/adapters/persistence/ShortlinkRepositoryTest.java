package dev.richryl.shortlink.adapters.persistence;

import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        Shortlink shortlink = new Shortlink(
                id,
                "https://example.com",
                "exmpl"
        );
        shortlinkRepository.save(shortlink);
        Shortlink retrievedShortlink = shortlinkRepository.findByShortCode("exmpl").orElse(null);

        assertNotNull(retrievedShortlink);
        assertEquals(retrievedShortlink, shortlink);
    }

    @Test
    @DisplayName("Should save and find shortlink by id")
    void saveAndFindById() {
        UUID id = UUID.randomUUID();
        Shortlink shortlink = new Shortlink(
                id,
                "https://example.com",
                "exmpl"
        );
        shortlinkRepository.save(shortlink);
        Shortlink retrievedShortlink = shortlinkRepository.findById(id).orElse(null);
        assertNotNull(retrievedShortlink);
        assertEquals(retrievedShortlink, shortlink);
    }

    @Test
    @DisplayName("Should update a shortlink given its id")
    void updateShortlink() {
        UUID id = UUID.randomUUID();
        Shortlink shortlink = new Shortlink(
                id,
                "https://example.com",
                "exmpl"
        );
        shortlinkRepository.save(shortlink);

        Shortlink updatedShortlink = new Shortlink(
                id,
                "https://updatedexample.com",
                "exmpl"
        );
        Shortlink result = shortlinkRepository.update(updatedShortlink).orElse(null);

        assertNotNull(result);
        assertEquals("https://updatedexample.com", result.getOriginalUrl());
        assertEquals("exmpl", result.getShortCode());
    }

}
