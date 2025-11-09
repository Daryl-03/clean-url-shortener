package dev.richryl.shortlink.adapters.web;

import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;
import dev.richryl.shortlink.application.ports.in.ResolveShortlinkUseCase;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/s")
public class RedirectController {
    private final ResolveShortlinkUseCase resolveShortlinkUseCase;

    public RedirectController(ResolveShortlinkUseCase resolveShortlinkUseCase) {
        this.resolveShortlinkUseCase = resolveShortlinkUseCase;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectToOriginalUrl(@PathVariable String shortCode) {
        ShortlinkResponse shortlink = resolveShortlinkUseCase.handle(shortCode);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", shortlink.originalUrl());
        return ResponseEntity.status(HttpStatus.FOUND).headers(headers).build();
    }
}
