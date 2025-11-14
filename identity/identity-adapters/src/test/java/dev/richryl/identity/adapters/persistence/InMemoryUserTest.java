package dev.richryl.identity.adapters.persistence;

import dev.richryl.identity.application.ports.out.UserRepository;

public class InMemoryUserTest extends UserRepositoryTest{
    @Override
    protected UserRepository getUserRepositoryImplementation() {
        return new InMemoryUserRepository();
    }
}
