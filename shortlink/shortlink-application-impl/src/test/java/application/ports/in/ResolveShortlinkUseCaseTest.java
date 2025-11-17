package application.ports.in;

import application.mocks.FakeShortlinkRepository;
import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.exceptions.ShortlinkNotFoundException;
import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;
import dev.richryl.shortlink.application.ports.in.ResolveShortlinkInteractor;
import dev.richryl.shortlink.application.ports.in.ResolveShortlinkUseCase;
import dev.richryl.shortlink.application.ports.out.AnalyticsPort;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ResolveShortlinkUseCaseTest {
    private final ShortlinkRepository shortlinkRepository = new FakeShortlinkRepository();
    private final AnalyticsPort analyticsPort = Mockito.mock(AnalyticsPort.class);
    private final ResolveShortlinkUseCase resolveShortlinkUseCase = new ResolveShortlinkInteractor(shortlinkRepository, analyticsPort);
    UUID shortlinkId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        shortlinkRepository.save(
                new Shortlink(shortlinkId, "http://example.com", "exmpl", UUID.randomUUID(), Instant.now(), Instant.now())
        );
        shortlinkRepository.save(
                new Shortlink(UUID.randomUUID(), "http://secondexample.com", "exmdel", UUID.randomUUID(), Instant.now(), Instant.now())
        );

    }

    @Test
    @DisplayName("Should resolve shortlink by short code")
    void shouldResolveShortlinkByShortCode() {

        ShortlinkResponse resolvedShortlink = resolveShortlinkUseCase.handle("exmpl");

        assertNotNull(resolvedShortlink);
        assertEquals("http://example.com", resolvedShortlink.originalUrl());
        Mockito.verify(analyticsPort, Mockito.times(1)).recordShortlinkAccess(shortlinkId);
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
