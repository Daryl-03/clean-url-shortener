package application.ports.in;

import application.mocks.FakeShortlinkRepository;
import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.ports.in.DeleteShortlinkByIdInteractor;
import dev.richryl.shortlink.application.ports.in.DeleteShortlinkByIdUseCase;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeleteShortlinkByIdUsecaseTest {
    private DeleteShortlinkByIdUseCase deleteShortlinkByIdUseCase;
    private final UUID existingId = UUID.randomUUID();
    private final ShortlinkRepository shortlinkRepository = new FakeShortlinkRepository(
                new ArrayList<>(List.of(
                        new Shortlink(existingId, "https://example.com", "abc123", UUID.randomUUID()),
                        new Shortlink(UUID.randomUUID(),"https://openai.com", "def456", UUID.randomUUID())
                ))
        );

    @BeforeEach
    void setup() {
        deleteShortlinkByIdUseCase = new DeleteShortlinkByIdInteractor(
                shortlinkRepository
        );
    }

    @Test
    @DisplayName("Should delete shortlink when given existing id")
    void should_delete_shortlink_when_given_existing_id() {

        deleteShortlinkByIdUseCase.handle(existingId);
                assertTrue(shortlinkRepository.findById(existingId).isEmpty());
    }

}
