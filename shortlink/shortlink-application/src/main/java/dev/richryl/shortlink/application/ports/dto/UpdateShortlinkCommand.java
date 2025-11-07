package dev.richryl.shortlink.application.ports.dto;

import java.util.UUID;

public record UpdateShortlinkCommand(
        UUID id,
        String url) {
}
