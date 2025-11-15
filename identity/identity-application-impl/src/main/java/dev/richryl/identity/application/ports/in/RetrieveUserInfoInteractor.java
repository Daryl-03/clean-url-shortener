package dev.richryl.identity.application.ports.in;

import dev.richryl.identity.application.exceptions.UserNotFoundException;
import dev.richryl.identity.application.ports.dto.UserInfoResponse;
import dev.richryl.identity.application.ports.out.LoggerPort;
import dev.richryl.identity.application.ports.out.UserRepository;
import dev.richryl.identity.domain.User;

import java.util.UUID;

public class RetrieveUserInfoInteractor implements  RetrieveUserInfoUseCase {

    private final UserRepository userRepository;
    private final LoggerPort loggerPort;

    public RetrieveUserInfoInteractor(UserRepository userRepository, LoggerPort loggerPort) {
        this.userRepository = userRepository;
        this.loggerPort = loggerPort;
    }

    @Override
    public UserInfoResponse handle(UUID internalId) {
        User user = userRepository.findById(internalId).orElseThrow(
                () -> {
                    loggerPort.error("User not found with internalId: " + internalId);
                    return new UserNotFoundException("User not found with internalId: " + internalId);
                }
        );
        return UserInfoResponse.fromDomain(user);
    }
}
