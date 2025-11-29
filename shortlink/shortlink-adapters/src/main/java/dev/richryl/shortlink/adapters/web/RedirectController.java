package dev.richryl.shortlink.adapters.web;

import dev.richryl.identity.application.ports.dto.CreateClickEventCommand;
import dev.richryl.identity.application.ports.in.CreateClickEventUseCase;

import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;
import dev.richryl.shortlink.application.ports.in.ResolveShortlinkUseCase;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/s")
public class RedirectController {
    private final ResolveShortlinkUseCase resolveShortlinkUseCase;
    private final CreateClickEventUseCase createClickEventUseCase;

    public RedirectController(ResolveShortlinkUseCase resolveShortlinkUseCase, CreateClickEventUseCase createClickEventUseCase) {
        this.resolveShortlinkUseCase = resolveShortlinkUseCase;
        this.createClickEventUseCase = createClickEventUseCase;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode, @RequestHeader HttpHeaders headers) {
        ShortlinkResponse shortlink = resolveShortlinkUseCase.handle(shortCode);
        CreateClickEventCommand command = new CreateClickEventCommand(
                shortlink.id(),
                headers.getFirst("User-Agent"),
                headers.getFirst("X-Forwarded-For"),
                headers.getFirst("Referer"),
                headers.getFirst("Accept-Language")
        );
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Location", shortlink.originalUrl());

        createClickEventUseCase.handle(command);
        return ResponseEntity.status(HttpStatus.FOUND).headers(responseHeaders).build();
    }
}
