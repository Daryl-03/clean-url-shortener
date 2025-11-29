package dev.richryl.shortlink.adapters.web;

import dev.richryl.identity.application.ports.dto.CreateClickEventCommand;
import dev.richryl.identity.application.ports.in.CreateClickEventUseCase;

import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;
import dev.richryl.shortlink.application.ports.in.ResolveShortlinkUseCase;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/s")
public class RedirectController {
    private final ResolveShortlinkUseCase resolveShortlinkUseCase;
    private final AsyncClickEventFacade asyncClickEventFacade;

    public RedirectController(ResolveShortlinkUseCase resolveShortlinkUseCase, AsyncClickEventFacade asyncClickEventFacade) {
        this.resolveShortlinkUseCase = resolveShortlinkUseCase;
        this.asyncClickEventFacade = asyncClickEventFacade;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode, HttpServletRequest request) {
        ShortlinkResponse shortlink = resolveShortlinkUseCase.handle(shortCode);
        CreateClickEventCommand command = new CreateClickEventCommand(
                shortlink.id(),
                request.getHeader("User-Agent"),
                request.getHeader("cf-connecting-ip"),
                request.getHeader("referer"),
                request.getHeader("Accept-Language")
        );

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Location", shortlink.originalUrl());

        asyncClickEventFacade.logClickEvent(command);
        return ResponseEntity.status(HttpStatus.FOUND).headers(responseHeaders).build();
    }
}
