package dev.richryl.shortlink.adapters.persistence;

import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        Shortlink shortlink = new Shortlink(
                "https://example.com",
                "exmpl"
        );
        shortlinkRepository.save(shortlink);
        Shortlink retrievedShortlink = shortlinkRepository.findByShortCode("exmpl").orElse(null);

        assertNotNull(retrievedShortlink);
        assertEquals(retrievedShortlink, shortlink);
    }

}
