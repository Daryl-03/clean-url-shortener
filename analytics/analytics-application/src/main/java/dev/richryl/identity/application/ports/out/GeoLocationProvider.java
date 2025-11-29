package dev.richryl.identity.application.ports.out;

import dev.richryl.analytics.domain.GeoLocation;

public interface GeoLocationProvider {
    GeoLocation getGeoLocation(String ipAddress);
}
