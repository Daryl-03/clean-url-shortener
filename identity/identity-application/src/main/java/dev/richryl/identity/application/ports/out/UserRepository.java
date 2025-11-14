package dev.richryl.identity.application.ports.out;

import dev.richryl.identity.application.ports.dto.UserInfoResponse;
import dev.richryl.identity.domain.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByExternalId(String externalId);
    void save(User user);
}
