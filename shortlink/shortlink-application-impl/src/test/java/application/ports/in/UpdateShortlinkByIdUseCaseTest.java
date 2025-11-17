package application.ports.in;

import application.mocks.FakeShortlinkRepository;
import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.exceptions.ShortlinkNotFoundException;
import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;
import dev.richryl.shortlink.application.ports.dto.UpdateShortlinkCommand;
import dev.richryl.shortlink.application.ports.in.UpdateShortlinkByIdInteractor;
import dev.richryl.shortlink.application.ports.in.UpdateShortlinkByIdUseCase;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateShortlinkByIdUseCaseTest {
    private final ShortlinkRepository shortlinkRepository = new FakeShortlinkRepository();
    private final UpdateShortlinkByIdUseCase updateShortlinkByIdUseCase = new UpdateShortlinkByIdInteractor(shortlinkRepository);
    private final UUID existingId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        Instant added = Instant.now().minusSeconds(3600 * 24); // 1 day ago
        shortlinkRepository.save(
            new Shortlink(existingId, "https://example.com", "abc123", UUID.randomUUID(), added, added)
        );
        shortlinkRepository.save(
            new Shortlink(UUID.randomUUID(), "https://secondexample.com", "abc123567", UUID.randomUUID(), added, added)
        );

    }

    @Test
    @DisplayName("Should update shortlink when given existing id and valid data")
    void should_update_shortlink_when_given_existing_id_and_valid_data() {
        String updatedUrl = "https://updatedexample.com";
        UpdateShortlinkCommand command = new UpdateShortlinkCommand(
            existingId,
            updatedUrl
        );
        ShortlinkResponse updatedShortlink = updateShortlinkByIdUseCase.handle(command);
        assertNotNull(updatedShortlink);
        assertEquals(updatedUrl, updatedShortlink.originalUrl());

        Shortlink updated = shortlinkRepository.findById(existingId).orElse(null);
        assertNotNull(updated);
        assertEquals(updatedUrl, updated.getOriginalUrl());
        assertEquals("abc123", updatedShortlink.shortCode());
        assertNotNull(updated.getUpdatedAt());
        assertNotEquals(updated.getCreatedAt().truncatedTo(ChronoUnit.SECONDS), updated.getUpdatedAt().truncatedTo(ChronoUnit.SECONDS));
    }

    @Test
    @DisplayName("Should throw shortlinkNotFound exception when given non-existing id")
    void should_throw_shortlinkNotFound_exception_when_given_non_existing_id() {
        UpdateShortlinkCommand command = new UpdateShortlinkCommand(
            UUID.randomUUID(),
            "https://nonexisting.com"
        );
        assertThrows( ShortlinkNotFoundException.class, ()->updateShortlinkByIdUseCase.handle(command));

    }

}
