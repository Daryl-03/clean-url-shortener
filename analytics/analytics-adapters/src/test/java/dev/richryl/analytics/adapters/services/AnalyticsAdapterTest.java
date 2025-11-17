package dev.richryl.analytics.adapters.services;

import dev.richryl.identity.application.ports.in.CreateClickEventUseCase;
import dev.richryl.shortlink.application.ports.out.AnalyticsPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.mockito.Mockito.*;

public class AnalyticsAdapterTest {
    private AnalyticsPort analyticsPort;

    @MockitoBean
    private CreateClickEventUseCase createClickEventUseCase;

    @BeforeEach
    void setUp() {
        createClickEventUseCase = mock(CreateClickEventUseCase.class);
        analyticsPort = new AnalyticsAdapter(createClickEventUseCase);
    }

    @Test
    @DisplayName("should call createClickEventUseCase when recordShortlinkAccess is invoked")
    void shouldRecordShortlinkAccess() {
        UUID shortlinkId = UUID.randomUUID();
        doNothing().when(createClickEventUseCase).handle(shortlinkId);
        analyticsPort.recordShortlinkAccess(shortlinkId);

        verify(createClickEventUseCase, times(1)).handle(shortlinkId);
    }
}
