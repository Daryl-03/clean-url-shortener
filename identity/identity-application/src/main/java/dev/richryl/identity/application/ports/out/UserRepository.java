package dev.richryl.identity.application.ports.out;

import dev.richryl.identity.application.ports.dto.UserInfoResponse;

import java.util.Optional;

public interface UserRepository {

    Optional<UserInfoResponse> findByExternalId(String externalId);
}
