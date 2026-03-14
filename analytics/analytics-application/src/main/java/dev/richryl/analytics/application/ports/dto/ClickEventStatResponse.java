package dev.richryl.analytics.application.ports.dto;

import java.util.List;

public record ClickEventStatResponse(
        int totalClicks,
        List<BrowserStat> browserStats,
        List<CountryStat> countryStats,
        List<ClickPerDayPerDeviceTypeStat> clicksPerDayPerDeviceType
) {
}

