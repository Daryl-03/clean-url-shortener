package dev.richryl.identity.application.ports.in;

import dev.richryl.identity.application.exceptions.UserNotFoundException;
import dev.richryl.identity.application.ports.dto.UserInfoResponse;
import dev.richryl.identity.application.ports.out.LoggerPort;
import dev.richryl.identity.application.ports.out.UserRepository;

public class RetrieveUserInfoInteractor implements  RetrieveUserInfoUseCase {

    private final UserRepository userRepository;
    private final LoggerPort loggerPort;

    public RetrieveUserInfoInteractor(UserRepository userRepository, LoggerPort loggerPort) {
        this.userRepository = userRepository;
        this.loggerPort = loggerPort;
    }

    @Override
    public UserInfoResponse handle(String externalId) {

        return userRepository.findByExternalId(externalId).orElseThrow(
                () -> {
                    loggerPort.error("User not found with externalId: " + externalId);
                    return new UserNotFoundException("User not found with externalId: " + externalId);
                }
        );
    }
}
