package application.ports.in;

import application.mocks.FakeShortlinkRepository;
import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.exceptions.ShortlinkNotFoundException;
import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;
import dev.richryl.shortlink.application.ports.in.GetShortlinkByIdInteractor;
import dev.richryl.shortlink.application.ports.in.GetShortlinkByIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GetShortlinkByIdUsecaseTest {

    private GetShortlinkByIdUseCase getShortlinkByIdUseCase;
    private final UUID firstId = UUID.randomUUID();

    @BeforeEach
    void setup() {
        FakeShortlinkRepository shortlinkRepository = new FakeShortlinkRepository(
                java.util.List.of(
                        new Shortlink( firstId,"https://example.com", "abc123", UUID.randomUUID(), Instant.now(), Instant.now()),
                        new Shortlink(UUID.randomUUID(),"https://openai.com", "def456", UUID.randomUUID(), Instant.now(), Instant.now())
                )
        );

        getShortlinkByIdUseCase = new GetShortlinkByIdInteractor(
                shortlinkRepository
        );
    }

    @Test
    @DisplayName("Should return shortlink when given existing id")
    void should_return_shortlink_when_given_existing_id() {

        ShortlinkResponse shortlink = getShortlinkByIdUseCase.handle(firstId);

        assertNotNull(shortlink);
        assertEquals(firstId, shortlink.id());
        assertEquals("https://example.com", shortlink.originalUrl());
    }

    @Test
        @DisplayName("Should throw exception when given non-existing id")
    void should_throw_exception_when_given_non_existing_id() {
        assertThrows(ShortlinkNotFoundException.class, () ->
            getShortlinkByIdUseCase.handle(UUID.randomUUID())
        );
    }

}
