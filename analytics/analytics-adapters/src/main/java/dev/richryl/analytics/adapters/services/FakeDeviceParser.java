package dev.richryl.analytics.adapters.services;

import dev.richryl.analytics.domain.DeviceInfo;
import dev.richryl.identity.application.ports.out.DeviceInfoParser;

public class FakeDeviceParser  implements DeviceInfoParser {
    @Override
    public DeviceInfo createDeviceInfo(String userAgent) {
        return new DeviceInfo(
                "FakeBrowser",
                "FakeOS",
                "FakeDevice"
        );
    }
}
