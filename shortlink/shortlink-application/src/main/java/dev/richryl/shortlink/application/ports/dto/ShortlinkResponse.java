package dev.richryl.shortlink.application.ports.dto;

import dev.richryl.shortlink.Shortlink;

import java.time.Instant;
import java.util.UUID;

public record ShortlinkResponse(UUID id, String originalUrl, String shortCode, Instant createdAt, Instant updatedAt) {
    public static ShortlinkResponse fromDomain(Shortlink shortlink) {
        return new ShortlinkResponse(
                shortlink.getId(),
                shortlink.getOriginalUrl(),
                shortlink.getShortCode(),
                shortlink.getCreatedAt(),
                shortlink.getUpdatedAt()
        );
    }
}
