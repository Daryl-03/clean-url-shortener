package dev.richryl.shortlink.adapters.web;

import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.adapters.web.dto.CreateShortlinkRequest;
import dev.richryl.shortlink.application.ports.in.CreateShortlinkUseCase;
import dev.richryl.shortlink.application.ports.in.DeleteShortlinkByShortcodeUseCase;
import dev.richryl.shortlink.application.ports.in.GetShortlinkByShortcodeUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shortlinks")
public class ShortlinkController {
    private final CreateShortlinkUseCase createShortlinkUseCase;
    private final GetShortlinkByShortcodeUseCase getShortlinkByShortcodeUseCase;
    private final DeleteShortlinkByShortcodeUseCase deleteShortlinkByShortcodeUseCase;

    public ShortlinkController(CreateShortlinkUseCase createShortlinkUseCase, GetShortlinkByShortcodeUseCase getShortlinkByShortcodeUseCase, DeleteShortlinkByShortcodeUseCase deleteShortlinkByShortcodeUseCase) {
        this.createShortlinkUseCase = createShortlinkUseCase;
        this.getShortlinkByShortcodeUseCase = getShortlinkByShortcodeUseCase;
        this.deleteShortlinkByShortcodeUseCase = deleteShortlinkByShortcodeUseCase;
    }

    @PostMapping()
    public ResponseEntity<Shortlink> createShortlink(@RequestBody @Valid CreateShortlinkRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createShortlinkUseCase.handle(request.url()));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Shortlink> getShortlink(@PathVariable("shortCode") String shortCode) {
        return ResponseEntity.status(HttpStatus.OK).body(getShortlinkByShortcodeUseCase.handle(shortCode));
    }

    @DeleteMapping("/{shortCode}")
    public ResponseEntity<Void> deleteShortlink(@PathVariable("shortCode") String shortCode) {
        deleteShortlinkByShortcodeUseCase.handle(shortCode);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
