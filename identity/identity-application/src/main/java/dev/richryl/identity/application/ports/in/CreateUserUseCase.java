package dev.richryl.identity.application.ports.in;

import dev.richryl.identity.application.ports.dto.UserInfoResponse;

public interface CreateUserUseCase {
    UserInfoResponse handle(String externalId);
}
