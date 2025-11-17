package application.ports.in;

import application.mocks.FakeShortlinkRepository;
import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.application.ports.in.RetrieveAllShortlinksForUserInteractor;
import dev.richryl.shortlink.application.ports.in.RetrieveAllShortlinksForUserUseCase;
import dev.richryl.shortlink.application.ports.out.ShortlinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RetrieveAllShortlinksForUserUseCaseTest {
    private RetrieveAllShortlinksForUserUseCase retrieveAllShortlinksForUserUseCase;
    private final UUID userId = UUID.randomUUID();

    @BeforeEach
    void setup() {
        ShortlinkRepository shortlinkRepository = new FakeShortlinkRepository(
                new ArrayList<>(
                        List.of(
                                new Shortlink(UUID.randomUUID(), "http://example.com", "exmpl", userId, Instant.now(), Instant.now()),
                                new Shortlink(UUID.randomUUID(), "http://secondexample.com", "exmdel", userId, Instant.now(), Instant.now()),
                                new Shortlink(UUID.randomUUID(), "http://otheruser.com", "othusr", UUID.randomUUID(), Instant.now(), Instant.now()
                                )
                        )
                ));
        retrieveAllShortlinksForUserUseCase = new RetrieveAllShortlinksForUserInteractor(shortlinkRepository);
    }

    @Test
    void should_retrieve_all_shortlinks_for_user() {
        var shortlinks = retrieveAllShortlinksForUserUseCase.handle(userId);
        assertEquals(2, shortlinks.size());
        assertEquals("http://example.com", shortlinks.get(0).originalUrl());
        assertEquals("http://secondexample.com", shortlinks.get(1).originalUrl());
    }
}
