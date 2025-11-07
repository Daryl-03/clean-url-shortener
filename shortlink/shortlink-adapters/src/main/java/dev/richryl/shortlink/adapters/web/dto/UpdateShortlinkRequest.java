package dev.richryl.shortlink.adapters.web.dto;

import dev.richryl.shortlink.application.ports.dto.UpdateShortlinkCommand;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

import java.util.UUID;


public record UpdateShortlinkRequest(
        @NotNull(message = "id should be provided")
        UUID id,
        @NotNull(message = "url should be provided")
        @URL(message = "Url format is invalid")
        String url
) {
    public UpdateShortlinkCommand toCommand() {
        return new UpdateShortlinkCommand(
                this.id,
                this.url
        );
    }
}
