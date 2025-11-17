package dev.richryl.shortlink.adapters.services;

import dev.richryl.shortlink.application.ports.out.AnalyticsPort;

import java.util.UUID;

public class FakeAnalyticsPort implements AnalyticsPort {


    @Override
    public void recordShortlinkAccess(UUID shortlinkId) {

    }
}
