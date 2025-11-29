package dev.richryl.analytics.adapters.web;

import dev.richryl.identity.application.ports.dto.ClickEventResponse;
import dev.richryl.identity.application.ports.in.RetrieveClickEventsUseCase;
import dev.richryl.identity.application.ports.in.RetrieveRangedClickEventsUseCase;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final RetrieveClickEventsUseCase retrieveClickEventsUseCase;
    private final RetrieveRangedClickEventsUseCase retrieveRangedClickEventsUseCase;

    public AnalyticsController(RetrieveClickEventsUseCase retrieveClickEventsUseCase, RetrieveRangedClickEventsUseCase retrieveRangedClickEventsUseCase) {
        this.retrieveClickEventsUseCase = retrieveClickEventsUseCase;
        this.retrieveRangedClickEventsUseCase = retrieveRangedClickEventsUseCase;
    }

    @GetMapping("/{urlId}")
    public List<ClickEventResponse> handle(@PathVariable UUID urlId) {
        return retrieveClickEventsUseCase.handle(urlId);
    }

    @GetMapping("/{urlId}/ranged")
    public List<ClickEventResponse> handle(@PathVariable UUID urlId, @RequestParam Instant from, @RequestParam Instant to) {
        return retrieveRangedClickEventsUseCase.handle(urlId, from, to);
    }
}
