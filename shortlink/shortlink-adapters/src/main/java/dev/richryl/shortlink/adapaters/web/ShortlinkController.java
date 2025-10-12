package dev.richryl.shortlink.adapaters.web;

import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.adapaters.web.dto.CreateShortlinkRequest;
import dev.richryl.shortlink.application.ports.in.CreateShortlinkUseCase;
import dev.richryl.shortlink.application.ports.in.GetShortlinkUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shortlinks")
public class ShortlinkController {
    private final CreateShortlinkUseCase createShortlinkUseCase;
    private final GetShortlinkUseCase getShortlinkUseCase;

    public ShortlinkController(CreateShortlinkUseCase createShortlinkUseCase, GetShortlinkUseCase getShortlinkUseCase) {
        this.createShortlinkUseCase = createShortlinkUseCase;
        this.getShortlinkUseCase = getShortlinkUseCase;
    }

    @PostMapping()
    public ResponseEntity<Shortlink> createShortlink(@RequestBody @Valid CreateShortlinkRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createShortlinkUseCase.handle(request.url()));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Shortlink> getShortlink(@PathVariable("shortCode") String shortCode) {
        return ResponseEntity.status(HttpStatus.OK).body(getShortlinkUseCase.handle(shortCode));
    }
}
