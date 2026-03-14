package dev.richryl.analytics.application.ports.out;

import dev.richryl.analytics.application.ports.dto.BrowserStat;
import dev.richryl.analytics.application.ports.dto.ClickPerDayPerDeviceTypeStat;
import dev.richryl.analytics.application.ports.dto.CountryStat;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ClickEventStatRepository {

    List<BrowserStat> retrieveBrowserStats(
            UUID shortlinkId,
            Instant from,
            Instant to
    );

    List<CountryStat> retrieveCountryStats(
            UUID shortlinkId,
            Instant from,
            Instant to
    );

    List<ClickPerDayPerDeviceTypeStat> retrieveClicksPerDayDeviceType(
            UUID shortlinkId,
            Instant from,
            Instant to
    );

    int getNumberOfClicksInPeriod(
            UUID shortlinkId,
            Instant from,
            Instant to
    );
}
