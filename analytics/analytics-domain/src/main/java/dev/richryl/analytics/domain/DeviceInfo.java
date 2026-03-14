package dev.richryl.analytics.domain;

public record DeviceInfo(
        String browser,
        String operatingSystem,
        String deviceType
) {
    public static DeviceInfo unknown() {
        return new DeviceInfo("Unknown", "Unknown", "Unknown");
    }
}
