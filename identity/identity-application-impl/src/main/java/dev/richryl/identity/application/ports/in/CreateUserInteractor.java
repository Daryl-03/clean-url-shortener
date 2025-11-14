package dev.richryl.identity.application.ports.in;

import dev.richryl.identity.application.ports.out.UserIdGenerator;
import dev.richryl.identity.application.ports.out.UserRepository;
import dev.richryl.identity.domain.User;

public class CreateUserInteractor implements CreateUserUseCase {
    private final UserRepository userRepository;
    private final UserIdGenerator userIdGenerator;

    public CreateUserInteractor(UserRepository userRepository, UserIdGenerator userIdGenerator) {
        this.userRepository = userRepository;
        this.userIdGenerator = userIdGenerator;
    }

    @Override
    public void handle(String externalId) {
        if(userRepository.findByExternalId(externalId).isPresent()) {
            return;
        }
        User user = new User(userIdGenerator.generate(), externalId);
        userRepository.save(user);
    }
}
