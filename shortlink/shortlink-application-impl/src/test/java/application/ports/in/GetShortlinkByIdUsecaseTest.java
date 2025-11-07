package application.ports.in;

import application.mocks.FakeShortlinkRepository;
import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.exceptions.ShortlinkNotFoundException;
import dev.richryl.shortlink.application.ports.in.GetShortlinkByIdInteractor;
import dev.richryl.shortlink.application.ports.in.GetShortlinkByIdUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class GetShortlinkByIdUsecaseTest {

    private GetShortlinkByIdUseCase getShortlinkByIdUseCase;
    private final UUID firstId = UUID.randomUUID();

    @BeforeEach
    void setup() {
        FakeShortlinkRepository shortlinkRepository = new FakeShortlinkRepository(
                java.util.List.of(
                        new Shortlink( firstId,"https://example.com", "abc123"),
                        new Shortlink(UUID.randomUUID(),"https://openai.com", "def456")
                )
        );

        getShortlinkByIdUseCase = new GetShortlinkByIdInteractor(
                shortlinkRepository
        );
    }

    @Test
    @DisplayName("Should return shortlink when given existing id")
    void should_return_shortlink_when_given_existing_id() {

        Shortlink shortlink = getShortlinkByIdUseCase.handle(firstId);

        assertNotNull(shortlink);
        assertEquals(firstId, shortlink.getId());
        assertEquals("https://example.com", shortlink.getOriginalUrl());
    }

    @Test
        @DisplayName("Should throw exception when given non-existing id")
    void should_throw_exception_when_given_non_existing_id() {
        assertThrows(ShortlinkNotFoundException.class, () ->
            getShortlinkByIdUseCase.handle(UUID.randomUUID())
        );
    }

}
