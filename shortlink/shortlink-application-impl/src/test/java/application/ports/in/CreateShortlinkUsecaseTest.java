package application.ports.in;

import application.mocks.FakeIdGenerator;
import application.mocks.FakeShortlinkRepository;
import application.mocks.FakeSlugGenerator;
import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;
import dev.richryl.shortlink.application.ports.in.CreateShortlinkInteractor;
import dev.richryl.shortlink.application.ports.in.CreateShortlinkUseCase;
import dev.richryl.shortlink.application.ports.out.ShortlinkIdGenerator;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;
import dev.richryl.shortlink.application.ports.out.SlugGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CreateShortlinkUsecaseTest {
    private final SlugGenerator slugGenerator = new FakeSlugGenerator();
    private final ShortlinkRepository shortlinkRepository = new FakeShortlinkRepository();
    private final ShortlinkIdGenerator shortlinkIdGenerator = new FakeIdGenerator();
    private final CreateShortlinkUseCase createShortlinkUseCase = new CreateShortlinkInteractor(
            slugGenerator,
            shortlinkRepository,
            shortlinkIdGenerator
    );

    private ShortlinkResponse computeShortlink(String url, UUID ownerId) {

        return createShortlinkUseCase.handle(url, ownerId);
    }

    @Test
    @DisplayName("Should create shortlink when given valid URL")
    void should_create_shortlink_when_given_validUrl() {
        String validUrl = "https://example.com";
        UUID validOwnerId = UUID.randomUUID();

        ShortlinkResponse shortlink = computeShortlink(validUrl, validOwnerId);

        assertNotNull(shortlink);
        assertEquals(validUrl, shortlink.originalUrl());
        assertNotNull(shortlink.shortCode());
        assertEquals(Instant.now().truncatedTo(ChronoUnit.MINUTES), shortlink.createdAt().truncatedTo(ChronoUnit.MINUTES));
        assertEquals(Instant.now().truncatedTo(ChronoUnit.MINUTES), shortlink.updatedAt().truncatedTo(ChronoUnit.MINUTES));
    }

    @Test
    @DisplayName("Should persist shortlink for later retrieval")
    void should_persist_shortlink_for_later_retrieval() {
        String validUrl = "https://example.com";
        UUID validOwnerId = UUID.randomUUID();

        ShortlinkResponse shortlink1 = computeShortlink(validUrl, validOwnerId);

        Shortlink retrievedShortlink = shortlinkRepository.findByShortCode(shortlink1.shortCode()).orElse(null);
        assertNotNull(retrievedShortlink);
        assertEquals(shortlink1.originalUrl(), retrievedShortlink.getOriginalUrl());
        assertEquals(shortlink1.shortCode(), retrievedShortlink.getShortCode());
        assertEquals(validOwnerId, retrievedShortlink.getOwnerId());
    }

    @Test
    @DisplayName("Should create unique short codes when the same URL is shortened multiple times")
    void should_create_unique_short_codes_when_same_url_shortened_multiple_times() {
        String validUrl = "https://example-to-be-shortened-multiple-times.com";
        UUID validOwnerId = UUID.randomUUID();

        ShortlinkResponse shortlink1 = computeShortlink(validUrl, validOwnerId);
        ShortlinkResponse shortlink2 = computeShortlink(validUrl, validOwnerId);

        assertNotNull(shortlink1);
        assertNotNull(shortlink2);
        assertNotEquals(shortlink1.shortCode(), shortlink2.shortCode());
    }

}