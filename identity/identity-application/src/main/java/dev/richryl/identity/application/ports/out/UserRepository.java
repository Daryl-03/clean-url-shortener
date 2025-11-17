package dev.richryl.identity.application.ports.out;

import dev.richryl.identity.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> findByExternalId(String externalId);
    void save(User user);

    Optional<User> findById(UUID internalId);
}
