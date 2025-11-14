package dev.richryl.identity.application.ports.in;

import dev.richryl.identity.application.ports.dto.UserInfoResponse;

public interface RetrieveUserInfoUseCase {
    UserInfoResponse handle(String externalId);
}
