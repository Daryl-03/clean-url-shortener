package dev.richryl.shortlink.application.ports.dto;

public record ClickData(
        String userAgent,
        String ipAddress,
        String referer,
        String acceptLanguage
) {
}
