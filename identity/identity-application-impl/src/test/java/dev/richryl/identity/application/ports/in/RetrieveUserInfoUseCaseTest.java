package dev.richryl.identity.application.ports.in;

import dev.richryl.identity.application.exceptions.UserNotFoundException;
import dev.richryl.identity.application.ports.dto.UserInfoResponse;
import dev.richryl.identity.application.ports.mocks.FakeUserRepository;
import dev.richryl.identity.application.ports.out.LoggerPort;
import dev.richryl.identity.application.ports.out.UserRepository;

import dev.richryl.identity.domain.User;
import org.mockito.Mockito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RetrieveUserInfoUseCaseTest {

    private RetrieveUserInfoUseCase retrieveUserInfoUseCase;
    private UUID firtstUserId = UUID.randomUUID();
    private LoggerPort loggerPort;

    @BeforeEach
    void setup() {
        UserRepository userRepository = new FakeUserRepository(
                new ArrayList<User>(
                        List.of(
                                new User(firtstUserId, "external-id-1"),
                                new User(UUID.randomUUID(),"external-id-2")
                        )
                )
        );

        loggerPort = Mockito.mock(LoggerPort.class);


        retrieveUserInfoUseCase = new RetrieveUserInfoInteractor(
                userRepository,
                loggerPort
        );
    }

    @Test
    @DisplayName("Should return user information for a given external ID")
    void shouldReturnUserInfoForGivenExternalId() {
        UserInfoResponse user = retrieveUserInfoUseCase.handle("external-id-1");
        assertEquals(user.id(), firtstUserId);
    }

    @Test
    @DisplayName("Should throw UserNotFound if user with given external ID does not exist")
    void shouldThrowUserNotFoundIfUserDoesNotExist() {
        assertThrows(UserNotFoundException.class, () -> {
            retrieveUserInfoUseCase.handle("non-existing-external-id");
        });
    }

    @Test
    @DisplayName("Should log appropriate message when user is not found")
    void shouldLogMessageWhenUserNotFound() {
        try {
            retrieveUserInfoUseCase.handle("non-existing-external-id");
        } catch (UserNotFoundException e) {}
        Mockito.verify(loggerPort, Mockito.times(1)).error(Mockito.anyString());
       // verify the string parameter contains id

    }
}
