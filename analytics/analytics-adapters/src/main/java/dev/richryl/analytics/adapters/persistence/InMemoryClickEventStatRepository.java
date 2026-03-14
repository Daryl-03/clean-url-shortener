package dev.richryl.analytics.adapters.persistence;

import dev.richryl.analytics.application.ports.dto.BrowserStat;
import dev.richryl.analytics.application.ports.dto.ClickPerDayPerDeviceTypeStat;
import dev.richryl.analytics.application.ports.dto.CountryStat;
import dev.richryl.analytics.application.ports.out.ClickEventRepository;
import dev.richryl.analytics.application.ports.out.ClickEventStatRepository;
import dev.richryl.analytics.domain.GeoLocation;

import java.time.Instant;
import java.util.*;

public class InMemoryClickEventStatRepository implements ClickEventStatRepository{
    private final ClickEventRepository clickEventRepository;

    public InMemoryClickEventStatRepository(ClickEventRepository clickEventRepository) {
        this.clickEventRepository = clickEventRepository;
    }

    @Override
    public List<BrowserStat> retrieveBrowserStats(UUID shortlinkId, Instant from, Instant to) {
        List<BrowserStat> browserStats = new ArrayList<>();
        var clicks = clickEventRepository.findByTimestampBetween(shortlinkId,from, to);
        Map<String, Integer> browsers = new HashMap<>();
        clicks.forEach(click -> {
            String browser = click.getDevice().browser();
            browsers.put(browser, browsers.getOrDefault(browser, 0) + 1);
        });
        browsers.forEach((browser, count) -> browserStats.add(new BrowserStat(browser, count)));
        return browserStats;
    }

    @Override
    public List<CountryStat> retrieveCountryStats(UUID shortlinkId, Instant from, Instant to) {
        List<CountryStat> countryStats = new ArrayList<>();
        var clicks = clickEventRepository.findByTimestampBetween(shortlinkId,from, to);
        Map<GeoLocation, Integer> countries = new HashMap<>();
        clicks.forEach(click -> {
            GeoLocation country = click.getLocation();
            countries.put(country, countries.getOrDefault(country, 0) + 1);
        });
        countries.forEach((country, count) -> countryStats.add(new CountryStat(country.countryName(), country.city(), count)));
        return countryStats;
    }

    @Override
    public List<ClickPerDayPerDeviceTypeStat> retrieveClicksPerDayDeviceType(UUID shortlinkId, Instant from, Instant to) {
        List<ClickPerDayPerDeviceTypeStat> clicksPerDayDeviceTypeStats = new ArrayList<>();
        var clicks = clickEventRepository.findByTimestampBetween(shortlinkId,from, to);
        Map<String, Map<String, Integer>> dateDeviceTypeMap = new HashMap<>();
        clicks.forEach(click -> {
            String date = click.getTimestamp().toString().substring(0,10); // YYYY-MM-DD
            String deviceType = click.getDevice().deviceType();
            dateDeviceTypeMap.putIfAbsent(date, new HashMap<>());
            Map<String, Integer> deviceTypeMap = dateDeviceTypeMap.get(date);
            initializeDeviceTypeMap(deviceTypeMap);
            deviceTypeMap.put(
                    deviceType.equalsIgnoreCase("desktop") || deviceType.equalsIgnoreCase("mobile") ? deviceType.toLowerCase() : "others",
                    deviceTypeMap.getOrDefault(deviceType, 0) + 1);
        });
        dateDeviceTypeMap.forEach((date, deviceTypeMap) -> {
            clicksPerDayDeviceTypeStats.add(new ClickPerDayPerDeviceTypeStat(
                    Instant.parse(date + "T00:00:00Z"),
                    deviceTypeMap));
        });
        return clicksPerDayDeviceTypeStats;
    }

    private void initializeDeviceTypeMap(Map<String, Integer> deviceTypeMap) {
        deviceTypeMap.put("desktop", 0);
        deviceTypeMap.put("mobile", 0);
        deviceTypeMap.put("other", 0);
    }

    @Override
    public int getNumberOfClicksInPeriod(UUID shortlinkId, Instant from, Instant to) {
        return clickEventRepository.findByTimestampBetween(shortlinkId,from,to).size();
    }
}
