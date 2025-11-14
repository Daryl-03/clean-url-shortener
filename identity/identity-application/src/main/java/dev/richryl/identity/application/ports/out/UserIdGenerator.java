package dev.richryl.identity.application.ports.out;

import java.util.UUID;

public interface UserIdGenerator {
    UUID generate();
}
