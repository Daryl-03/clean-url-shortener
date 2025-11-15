package dev.richryl.identity.application.ports.in;

import dev.richryl.identity.application.ports.dto.UserInfoResponse;
import dev.richryl.identity.application.ports.mocks.FakeUserRepository;
import dev.richryl.identity.application.ports.out.UserIdGenerator;
import dev.richryl.identity.application.ports.out.UserRepository;
import dev.richryl.identity.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CreateUserUseCaseTest {

    private CreateUserUseCase createUserUseCase;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new FakeUserRepository();
        UserIdGenerator idGenerator = UUID::randomUUID;
        createUserUseCase = new CreateUserInteractor(userRepository, idGenerator);
    }

    @Test
    @DisplayName("Should create a new user with the given external ID")
    void shouldCreateNewUserWithGivenExternalId() {
        UserInfoResponse created = createUserUseCase.handle("new-external-id");
        assertNotNull(created);
        assertEquals("new-external-id", created.externalId());
        User user = userRepository.findByExternalId("new-external-id").orElseThrow();
        assertNotNull(user);
        assertEquals("new-external-id", user.getExternalId());
    }
}
