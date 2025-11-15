package dev.richryl.identity.application.ports.in;

import dev.richryl.identity.application.ports.dto.UserInfoResponse;
import dev.richryl.identity.application.ports.out.UserIdGenerator;
import dev.richryl.identity.application.ports.out.UserRepository;
import dev.richryl.identity.domain.User;

import java.util.Optional;

public class CreateUserInteractor implements CreateUserUseCase {
    private final UserRepository userRepository;
    private final UserIdGenerator userIdGenerator;

    public CreateUserInteractor(UserRepository userRepository, UserIdGenerator userIdGenerator) {
        this.userRepository = userRepository;
        this.userIdGenerator = userIdGenerator;
    }

    @Override
    public UserInfoResponse handle(String externalId) {
        Optional<User> user = userRepository.findByExternalId(externalId);
        if(user.isPresent()) {
            return UserInfoResponse.fromDomain(user.get());
        }
        User newUser = new User(userIdGenerator.generate(), externalId);
        userRepository.save(newUser);
        return UserInfoResponse.fromDomain(newUser);
    }
}
