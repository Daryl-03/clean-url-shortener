package dev.richryl.identity.application.ports.out;

import dev.richryl.analytics.domain.DeviceInfo;

public interface DeviceInfoParser {
    DeviceInfo createDeviceInfo(String userAgent);
}
