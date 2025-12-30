package dev.richryl.shortlink.adapters.web;

import dev.richryl.analytics.application.ports.dto.CreateClickEventCommand;
import dev.richryl.analytics.application.ports.in.CreateClickEventUseCase;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncClickEventFacade {

    private final CreateClickEventUseCase delegate;

    public AsyncClickEventFacade(CreateClickEventUseCase delegate) {
        this.delegate = delegate;
    }

    @Async
    public void logClickEvent(CreateClickEventCommand command) {
        delegate.handle(command);
    }
}
