package dev.richryl.identity.application.ports.in;

public interface CreateUserUseCase {
    void handle(String externalId);
}
