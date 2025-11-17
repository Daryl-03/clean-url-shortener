package dev.richryl.common.adapters.services;


import dev.richryl.identity.application.ports.out.ClickEventIdGenerator;
import dev.richryl.identity.application.ports.out.UserIdGenerator;
import dev.richryl.shortlink.application.ports.out.ShortlinkIdGenerator;

import java.util.UUID;

public class UuidIdGenerator implements ShortlinkIdGenerator, UserIdGenerator, ClickEventIdGenerator {

    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }
}
