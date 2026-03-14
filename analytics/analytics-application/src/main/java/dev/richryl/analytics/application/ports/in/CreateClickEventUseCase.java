package dev.richryl.analytics.application.ports.in;


import dev.richryl.analytics.application.ports.dto.CreateClickEventCommand;



public interface CreateClickEventUseCase {
    void handle(CreateClickEventCommand command);
}
