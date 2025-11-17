package dev.richryl.analytics.application.ports.in;

import dev.richryl.analytics.application.ports.mocks.FakeClickEventRepository;
import dev.richryl.analytics.domain.ClickEvent;
import dev.richryl.identity.application.ports.in.CreateClickEventUseCase;
import dev.richryl.identity.application.ports.out.ClickEventIdGenerator;
import dev.richryl.identity.application.ports.out.ClickEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreateClickEventUseCaseTest {
    private ClickEventRepository clickEventRepository = new FakeClickEventRepository();
    private CreateClickEventUseCase createClickEventUseCase;

    @BeforeEach
    void setUp() {
        ClickEventIdGenerator idGenerator = UUID::randomUUID;
        createClickEventUseCase = new CreateClickEventInteractor(clickEventRepository, idGenerator);
    }

    @Test
    @DisplayName("should create a click event for a given shortcode")
    void shouldCreateClickEventForGivenlinkId() {
        UUID shortlinkId = UUID.randomUUID();
        createClickEventUseCase.handle(shortlinkId);

        List<ClickEvent> retrievedEvents = clickEventRepository.findByShortlinkId(shortlinkId);
        assertEquals(1, retrievedEvents.size());
        assertEquals(shortlinkId, retrievedEvents.get(0).getShortlinkId());
    }
}
