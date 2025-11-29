package dev.richryl.analytics.adapters.services;

import dev.richryl.analytics.domain.DeviceInfo;
import dev.richryl.identity.application.ports.out.DeviceInfoParser;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

public class YauaaDeviceParser implements DeviceInfoParser {

    private final UserAgentAnalyzer uaa = UserAgentAnalyzer
            .newBuilder()
            .hideMatcherLoadStats()
            .withCache(10000)
            .build();

    @Override
    public DeviceInfo createDeviceInfo(String userAgent) {
        UserAgent agent = uaa.parse(userAgent);
        return new DeviceInfo(
                agent.getValue(UserAgent.AGENT_NAME),
                agent.getValue(UserAgent.OPERATING_SYSTEM_NAME),
                agent.getValue(UserAgent.OPERATING_SYSTEM_CLASS)
        );
    }
}
