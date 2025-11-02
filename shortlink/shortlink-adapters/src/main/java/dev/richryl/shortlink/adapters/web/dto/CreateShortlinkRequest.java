package dev.richryl.shortlink.adapters.web.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;


public record CreateShortlinkRequest(
   @NotNull(message = "url should be provided")
   @URL(message = "Url format is invalid")
   String url
) {
}
