package application.ports.in;

import application.mocks.FakeShortlinkRepository;
import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.exceptions.ShortlinkNotFoundException;
import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;
import dev.richryl.shortlink.application.ports.in.ResolveShortlinkInteractor;
import dev.richryl.shortlink.application.ports.in.ResolveShortlinkUseCase;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ResolveShortlinkUseCaseTest {
    private final ShortlinkRepository shortlinkRepository = new FakeShortlinkRepository();
    private final ResolveShortlinkUseCase resolveShortlinkUseCase = new ResolveShortlinkInteractor(shortlinkRepository);

    @BeforeEach
    void setUp() {
        shortlinkRepository.save(
                new Shortlink(UUID.randomUUID(), "http://example.com", "exmpl", UUID.randomUUID())
        );
        shortlinkRepository.save(
                new Shortlink(UUID.randomUUID(), "http://secondexample.com", "exmdel", UUID.randomUUID())
        );

    }

    @Test
    @DisplayName("Should resolve shortlink by short code")
    void shouldResolveShortlinkByShortCode() {
        ShortlinkResponse resolvedShortlink = resolveShortlinkUseCase.handle("exmpl");

        assertNotNull(resolvedShortlink);
        assertEquals("http://example.com", resolvedShortlink.originalUrl());

    }

    @Test
    @DisplayName("Should throw exception when short code not found")
    void shouldThrowExceptionWhenShortCodeNotFound() {
        assertThrows(
                ShortlinkNotFoundException.class,
                () -> resolveShortlinkUseCase.handle("nonexistent")
        );
    }
}
