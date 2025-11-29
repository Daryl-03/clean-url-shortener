package dev.richryl.analytics.domain;

public record GeoLocation(
        String countryCode,
        String countryName,
        String city
) {
    public static GeoLocation unknown() {
        return new GeoLocation("UNKNOWN", "Unknown", "Unknown");
    }
}
