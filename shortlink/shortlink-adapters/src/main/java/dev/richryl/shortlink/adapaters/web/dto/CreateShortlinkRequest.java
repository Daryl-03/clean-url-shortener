package dev.richryl.shortlink.adapaters.web.dto;

import jakarta.validation.constraints.NotNull;

public record CreateShortlinkRequest(
   @NotNull String url
) {
}
