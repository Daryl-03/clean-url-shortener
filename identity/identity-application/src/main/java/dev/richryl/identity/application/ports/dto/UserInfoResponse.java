package dev.richryl.identity.application.ports.dto;

import dev.richryl.identity.domain.User;

import java.util.UUID;

public record UserInfoResponse(
        UUID id
) {
    public static UserInfoResponse fromDomain(User user) {
        return new UserInfoResponse(user.getId());
    }
}
