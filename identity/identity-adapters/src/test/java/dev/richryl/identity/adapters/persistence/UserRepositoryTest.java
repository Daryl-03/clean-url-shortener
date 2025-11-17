package dev.richryl.identity.adapters.persistence;

import dev.richryl.identity.application.ports.out.UserRepository;
import dev.richryl.identity.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository = getUserRepositoryImplementation();
    }

    protected abstract UserRepository getUserRepositoryImplementation();

    @Test
    void saveAndFindByExternalId() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "external-id-test");
        userRepository.save(user);
        User retrievedUser = userRepository.findByExternalId("external-id-test").orElse(null);
        assertNotNull(retrievedUser);
        assertEquals(retrievedUser.getId(), id);
        assertEquals(retrievedUser.getExternalId(), user.getExternalId());
    }

    @Test
    void saveAndFindById() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "external-id-test-2");
        userRepository.save(user);
        User retrievedUser = userRepository.findById(id).orElse(null);
        assertNotNull(retrievedUser);
        assertEquals(retrievedUser.getId(), id);
        assertEquals(retrievedUser.getExternalId(), user.getExternalId());
    }
}
