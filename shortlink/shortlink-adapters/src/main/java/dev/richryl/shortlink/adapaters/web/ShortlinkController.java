package dev.richryl.shortlink.adapaters.web;

import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.adapaters.web.dto.CreateShortlinkRequest;
import dev.richryl.shortlink.application.ports.in.CreateShortlinkUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shortlinks")
public class ShortlinkController {
    private final CreateShortlinkUseCase createShortlinkUseCase;

    public ShortlinkController(CreateShortlinkUseCase createShortlinkUseCase) {
        this.createShortlinkUseCase = createShortlinkUseCase;
    }

    @PostMapping()
    public ResponseEntity<Shortlink> createShortlink(@RequestBody CreateShortlinkRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createShortlinkUseCase.handle(request.url()));
    }
}
