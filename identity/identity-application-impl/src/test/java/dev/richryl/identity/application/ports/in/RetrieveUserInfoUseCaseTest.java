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
import static org.mockito.Mockito.verify;

public class RetrieveUserInfoUseCaseTest {

    private RetrieveUserInfoUseCase retrieveUserInfoUseCase;
    private final UUID firtstUserId = UUID.randomUUID();
    private LoggerPort loggerPort;

    @BeforeEach
    void setup() {
        UserRepository userRepository = new FakeUserRepository(
                new ArrayList<>(
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
    @DisplayName("Should return user information for a given internal ID")
    void shouldReturnUserInfoForGivenInternalId() {
        UserInfoResponse user = retrieveUserInfoUseCase.handle(firtstUserId);
        assertEquals(user.id(), firtstUserId);
        assertEquals("external-id-1", user.externalId());
    }

    @Test
    @DisplayName("Should throw UserNotFound if user with given internal ID does not exist")
    void shouldThrowUserNotFoundIfUserDoesNotExist() {
        assertThrows(UserNotFoundException.class, () ->
            retrieveUserInfoUseCase.handle(UUID.randomUUID())
        );
    }

    @Test
    @DisplayName("Should log appropriate message when user is not found")
    void shouldLogMessageWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        try {
            retrieveUserInfoUseCase.handle(userId);
        } catch (UserNotFoundException ignored) {}
        verify(loggerPort, Mockito.times(1)).error(Mockito.anyString());
        verify(loggerPort).error(Mockito.contains(userId.toString()));
    }
}
