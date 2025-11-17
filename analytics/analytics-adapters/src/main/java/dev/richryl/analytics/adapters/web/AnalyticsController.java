package dev.richryl.analytics.adapters.web;

import dev.richryl.identity.application.ports.dto.ClickEventResponse;
import dev.richryl.identity.application.ports.in.RetrieveClickEventsUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final RetrieveClickEventsUseCase retrieveClickEventsUseCase;

    public AnalyticsController(RetrieveClickEventsUseCase retrieveClickEventsUseCase) {
        this.retrieveClickEventsUseCase = retrieveClickEventsUseCase;
    }

    @GetMapping("/{urlId}")
    public List<ClickEventResponse> handle(@PathVariable UUID urlId) {
        return retrieveClickEventsUseCase.handle(urlId);
    }
}
