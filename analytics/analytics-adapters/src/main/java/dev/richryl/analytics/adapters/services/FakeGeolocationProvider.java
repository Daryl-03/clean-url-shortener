package dev.richryl.analytics.adapters.services;

import dev.richryl.analytics.domain.GeoLocation;
import dev.richryl.analytics.application.ports.out.GeoLocationProvider;

public class FakeGeolocationProvider implements GeoLocationProvider {
    @Override
    public GeoLocation getGeoLocation(String ipAddress) {
        return GeoLocation.unknown();
    }
}
