package dev.richryl.identity.application.ports.in;


import dev.richryl.identity.application.ports.dto.CreateClickEventCommand;



public interface CreateClickEventUseCase {
    void handle(CreateClickEventCommand command);
}
