package dev.richryl.analytics.domain;

public record GeoLocation(
        String countryCode,
        String countryName,
        String city
) {
    public static GeoLocation unknown() {
        return new GeoLocation("UNKNOWN", "Unknown", "Unknown");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GeoLocation that = (GeoLocation) obj;
        return countryCode.equals(that.countryCode) &&
                countryName.equals(that.countryName) &&
                city.equals(that.city);
    }
}
