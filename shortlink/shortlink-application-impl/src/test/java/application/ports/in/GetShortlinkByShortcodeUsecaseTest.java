package application.ports.in;

import application.mocks.FakeShortlinkRepository;
import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.exceptions.ShortlinkNotFoundException;
import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;

import dev.richryl.shortlink.application.ports.in.GetShortlinkByShortcodeInteractor;
import dev.richryl.shortlink.application.ports.in.GetShortlinkByShortcodeUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GetShortlinkByShortcodeUsecaseTest {

    private GetShortlinkByShortcodeUseCase getShortlinkByShortcodeUseCase;
    private final UUID firstId = UUID.randomUUID();

    @BeforeEach
    void setup() {
        FakeShortlinkRepository shortlinkRepository = new FakeShortlinkRepository(
                java.util.List.of(
                        new Shortlink( firstId,"https://example.com", "abc123", UUID.randomUUID(), Instant.now(), Instant.now()),
                        new Shortlink(UUID.randomUUID(),"https://openai.com", "def456", UUID.randomUUID(), Instant.now(), Instant.now())
                )
        );

        getShortlinkByShortcodeUseCase = new GetShortlinkByShortcodeInteractor(
                shortlinkRepository
        );
    }

    @Test
    @DisplayName("Should return shortlink when given existing shortcode")
    void should_return_shortlink_when_given_existing_id() {

        ShortlinkResponse shortlink = getShortlinkByShortcodeUseCase.handle("abc123");

        assertNotNull(shortlink);
        assertEquals(firstId, shortlink.id());
        assertEquals("https://example.com", shortlink.originalUrl());
    }

    @Test
        @DisplayName("Should throw exception when given non-existing id")
    void should_throw_exception_when_given_non_existing_id() {
        assertThrows(ShortlinkNotFoundException.class, () ->
            getShortlinkByShortcodeUseCase.handle("nonexistent")
        );
    }

}
