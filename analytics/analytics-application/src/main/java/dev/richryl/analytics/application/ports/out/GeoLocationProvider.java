package dev.richryl.analytics.application.ports.out;

import dev.richryl.analytics.domain.GeoLocation;

public interface GeoLocationProvider {
    GeoLocation getGeoLocation(String ipAddress);
}
