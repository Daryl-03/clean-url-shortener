package application.ports.in;

import application.mocks.FakeShortlinkRepository;
import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.exceptions.ShortlinkNotFoundException;
import dev.richryl.shortlink.application.ports.in.GetShortlinkInteractor;
import dev.richryl.shortlink.application.ports.in.GetShortlinkUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GetShortlinkUsecaseTest {

    private GetShortlinkUseCase getShortlinkUseCase;

    @BeforeEach
    void setup() {
        FakeShortlinkRepository shortlinkRepository = new FakeShortlinkRepository(
                java.util.List.of(
                        new Shortlink( "https://example.com", "abc123"),
                        new Shortlink("https://openai.com", "def456")
                )
        );

        getShortlinkUseCase = new GetShortlinkInteractor(
                shortlinkRepository
        );
    }

    @Test
    @DisplayName("Should return shortlink when given existing short code")
    void should_return_shortlink_when_given_existing_short_code() {
        String existingShortCode = "abc123";

        Shortlink shortlink = getShortlinkUseCase.handle(existingShortCode);

        assertNotNull(shortlink);
        assertEquals(existingShortCode, shortlink.getShortCode());
        assertEquals("https://example.com", shortlink.getOriginalUrl());
    }

    @Test
    @DisplayName("Should throw exception when given non-existing short code")
    void should_throw_exception_when_given_non_existing_short_code() {
        String nonExistingShortCode = "xyz789";
        assertThrows(ShortlinkNotFoundException.class, () ->
            getShortlinkUseCase.handle(nonExistingShortCode)
        );
    }

}
