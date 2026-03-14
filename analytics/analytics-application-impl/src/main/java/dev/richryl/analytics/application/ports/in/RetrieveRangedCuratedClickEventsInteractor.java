package dev.richryl.analytics.application.ports.in;

import dev.richryl.analytics.application.ports.dto.ClickEventStatResponse;
import dev.richryl.analytics.application.ports.out.ClickEventStatRepository;

import java.time.Instant;
import java.util.UUID;

public class RetrieveRangedCuratedClickEventsInteractor implements RetrieveRangedCuratedClickEventsUseCase {

    private final ClickEventStatRepository clickEventStatRepository;

    public RetrieveRangedCuratedClickEventsInteractor(ClickEventStatRepository clickEventStatRepository) {
        this.clickEventStatRepository = clickEventStatRepository;
    }

    @Override
    public ClickEventStatResponse handle(UUID shortlinkId, Instant from, Instant to) {
        int numberOfClicks = clickEventStatRepository.getNumberOfClicksInPeriod(shortlinkId, from, to);
        var browserStats = clickEventStatRepository.retrieveBrowserStats(shortlinkId, from, to);
        var countryStats = clickEventStatRepository.retrieveCountryStats(shortlinkId, from, to);
        var clickPerDevicePerDay = clickEventStatRepository.retrieveClicksPerDayDeviceType(shortlinkId, from, to);

        return  new ClickEventStatResponse(
                numberOfClicks,
                browserStats,
                countryStats,
                clickPerDevicePerDay
        );
    }
}
