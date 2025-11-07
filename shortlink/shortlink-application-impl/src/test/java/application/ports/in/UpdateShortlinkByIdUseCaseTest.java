package application.ports.in;

import application.mocks.FakeShortlinkRepository;
import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.ports.dto.UpdateShortlinkCommand;
import dev.richryl.shortlink.application.ports.in.UpdateShortlinkByIdInteractor;
import dev.richryl.shortlink.application.ports.in.UpdateShortlinkByIdUseCase;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UpdateShortlinkByIdUseCaseTest {
    private final ShortlinkRepository shortlinkRepository = new FakeShortlinkRepository();
    private final UpdateShortlinkByIdUseCase updateShortlinkByIdUseCase = new UpdateShortlinkByIdInteractor(shortlinkRepository);
    private final UUID existingId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        shortlinkRepository.save(
            new Shortlink(existingId, "https://example.com", "abc123")
        );
        shortlinkRepository.save(
            new Shortlink(UUID.randomUUID(), "https://secondexample.com", "abc123567")
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
        Shortlink updatedShortlink = updateShortlinkByIdUseCase.handle(command);
        assertNotNull(updatedShortlink);
        assertEquals(updatedUrl, updatedShortlink.getOriginalUrl());

        Shortlink updated = shortlinkRepository.findById(existingId).orElse(null);
        assertNotNull(updated);
        assertEquals(updatedUrl, updated.getOriginalUrl());
        assertEquals("abc123", updatedShortlink.getShortCode());
    }

}
