package application.ports.in;

import application.mocks.FakeShortlinkRepository;
import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.ports.in.DeleteShortlinkByShortcodeInteractor;
import dev.richryl.shortlink.application.ports.in.DeleteShortlinkByShortcodeUseCase;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeleteShortlinkByShortcodeUsecaseTest {
    private DeleteShortlinkByShortcodeUseCase deleteShortlinkByShortcodeUseCase;
    private final ShortlinkRepository shortlinkRepository = new FakeShortlinkRepository(
                new ArrayList<>(List.of(
                        new Shortlink( "https://example.com", "abc123"),
                        new Shortlink("https://openai.com", "def456")
                ))
        );

    @BeforeEach
    void setup() {
        deleteShortlinkByShortcodeUseCase = new DeleteShortlinkByShortcodeInteractor(
                shortlinkRepository
        );
    }

    @Test
    @DisplayName("Should delete shortlink when given existing short code")
    void should_delete_shortlink_when_given_existing_short_code() {
        String existingShortCode = "abc123";
        deleteShortlinkByShortcodeUseCase.handle(existingShortCode);
        assertTrue(shortlinkRepository.findByShortCode("abc123").isEmpty());
    }

}
