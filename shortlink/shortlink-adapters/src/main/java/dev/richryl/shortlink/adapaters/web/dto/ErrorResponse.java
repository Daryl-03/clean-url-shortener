package dev.richryl.shortlink.adapaters.web.dto;

public record ErrorResponse(
        int status,
        String code,
        String error
) {
}
