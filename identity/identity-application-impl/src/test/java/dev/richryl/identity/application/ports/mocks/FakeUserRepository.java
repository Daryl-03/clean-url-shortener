package dev.richryl.identity.application.ports.mocks;


import dev.richryl.identity.application.ports.out.UserRepository;
import dev.richryl.identity.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FakeUserRepository implements UserRepository {
    private final List<User> users;

    public FakeUserRepository(List<User> users) {
        this.users = users;
    }

    public FakeUserRepository() {
        this.users = new ArrayList<>();
    }

    @Override
    public Optional<User> findByExternalId(String externalId) {
        return users.stream()
                .filter(user -> user.getExternalId().equals(externalId))
                .findFirst();
    }

    @Override
    public void save(User user) {
        users.add(user);
    }
}
