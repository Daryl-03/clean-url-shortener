package dev.richryl.analytics.application.ports.out;

import dev.richryl.analytics.domain.DeviceInfo;

public interface DeviceInfoParser {
    DeviceInfo createDeviceInfo(String userAgent);
}
