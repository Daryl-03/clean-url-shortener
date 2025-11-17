package dev.richryl.identity.application.ports.in;

import dev.richryl.identity.application.ports.dto.UserInfoResponse;

import java.util.UUID;

public interface RetrieveUserInfoUseCase {
    UserInfoResponse handle(UUID internalId);
}
