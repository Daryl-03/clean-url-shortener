package dev.richryl.analytics.adapters.web;

import dev.richryl.analytics.application.ports.dto.ClickEventResponse;
import dev.richryl.analytics.application.ports.in.RetrieveClickEventsUseCase;
import dev.richryl.analytics.application.ports.in.RetrieveRangedClickEventsUseCase;
import dev.richryl.analytics.application.ports.in.RetrieveRangedCuratedClickEventsUseCase;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final RetrieveClickEventsUseCase retrieveClickEventsUseCase;
    private final RetrieveRangedClickEventsUseCase retrieveRangedClickEventsUseCase;
    private final RetrieveRangedCuratedClickEventsUseCase retrieveRangedCuratedClickEventsUseCase;

    public AnalyticsController(RetrieveClickEventsUseCase retrieveClickEventsUseCase, RetrieveRangedClickEventsUseCase retrieveRangedClickEventsUseCase, RetrieveRangedCuratedClickEventsUseCase retrieveRangedCuratedClickEventsUseCase) {
        this.retrieveClickEventsUseCase = retrieveClickEventsUseCase;
        this.retrieveRangedClickEventsUseCase = retrieveRangedClickEventsUseCase;
        this.retrieveRangedCuratedClickEventsUseCase = retrieveRangedCuratedClickEventsUseCase;
    }

    @GetMapping("/{urlId}")
    public List<ClickEventResponse> handle(@PathVariable UUID urlId) {
        return retrieveClickEventsUseCase.handle(urlId);
    }

    @GetMapping("/{urlId}/ranged")
    public List<ClickEventResponse> handle(@PathVariable UUID urlId, @RequestParam Instant from, @RequestParam Instant to) {
        return retrieveRangedClickEventsUseCase.handle(urlId, from, to);
    }

    @GetMapping("/{urlId}/curated")
    public Object handleCurated(@PathVariable UUID urlId, @RequestParam Instant from, @RequestParam Instant to) {
        return retrieveRangedCuratedClickEventsUseCase.handle(urlId, from, to);
    }
}
