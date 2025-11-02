package dev.richryl.shortlink.adapters.web.dto;

public record ErrorResponse(
        int status,
        String code,
        String error
) {
}
