package dev.richryl.shortlink.adapters.services;

import dev.richryl.shortlink.application.ports.out.ShortlinkIdGenerator;

import java.util.UUID;

public class UuidIdGenerator implements ShortlinkIdGenerator {

    @Override
    public UUID generate() {
        return UUID.randomUUID();
    }
}
