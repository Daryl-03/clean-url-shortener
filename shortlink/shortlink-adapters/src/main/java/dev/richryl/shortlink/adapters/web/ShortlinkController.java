package dev.richryl.shortlink.adapters.web;

import dev.richryl.shortlink.adapters.web.dto.CreateShortlinkRequest;
import dev.richryl.shortlink.adapters.web.dto.UpdateShortlinkRequest;
import dev.richryl.shortlink.application.ports.dto.ShortlinkResponse;
import dev.richryl.shortlink.application.ports.in.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/shortlinks")
public class ShortlinkController {
    private final CreateShortlinkUseCase createShortlinkUseCase;
    private final GetShortlinkByIdUseCase getShortlinkByIdUseCase;
    private final DeleteShortlinkByIdUseCase deleteShortlinkByIdUseCase;
    private final UpdateShortlinkByIdUseCase updateShortlinkByIdUseCase;
    private final RetrieveAllShortlinksForUserUseCase retrieveAllShortlinksForUserUseCase;
    private final ResolveShortlinkUseCase resolveShortlinkUseCase;

    public ShortlinkController(CreateShortlinkUseCase createShortlinkUseCase, GetShortlinkByIdUseCase getShortlinkByIdUseCase, DeleteShortlinkByIdUseCase deleteShortlinkByIdUseCase, UpdateShortlinkByIdUseCase updateShortlinkByIdUseCase, RetrieveAllShortlinksForUserUseCase retrieveAllShortlinksForUserUseCase, ResolveShortlinkUseCase resolveShortlinkUseCase) {
        this.createShortlinkUseCase = createShortlinkUseCase;
        this.getShortlinkByIdUseCase = getShortlinkByIdUseCase;
        this.deleteShortlinkByIdUseCase = deleteShortlinkByIdUseCase;
        this.updateShortlinkByIdUseCase = updateShortlinkByIdUseCase;
        this.retrieveAllShortlinksForUserUseCase = retrieveAllShortlinksForUserUseCase;
        this.resolveShortlinkUseCase = resolveShortlinkUseCase;
    }

    @PostMapping()
    public ResponseEntity<ShortlinkResponse> createShortlink(
            @RequestBody @Valid CreateShortlinkRequest request,
            Principal principal
    ) {
        UUID ownerId = UUID.fromString(principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(createShortlinkUseCase.handle(request.url(), ownerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShortlinkResponse> getShortlink(@PathVariable("id") UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(getShortlinkByIdUseCase.handle(id));
    }

    @GetMapping("/slug/{shortcode}")
    public ResponseEntity<ShortlinkResponse> getShortlink(@PathVariable("shortcode") String shortcode) {
        return ResponseEntity.status(HttpStatus.OK).body(resolveShortlinkUseCase.handle(shortcode));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShortlink(@PathVariable("id") UUID id) {
        deleteShortlinkByIdUseCase.handle(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping()
    public ResponseEntity<ShortlinkResponse> updateShortlink(@RequestBody @Valid UpdateShortlinkRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(updateShortlinkByIdUseCase.handle(request.toCommand()));
    }

    @GetMapping
    public ResponseEntity<List<ShortlinkResponse>> getShortlinks(
            Principal principal
    ) {
        UUID ownerId = UUID.fromString(principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(retrieveAllShortlinksForUserUseCase.handle(ownerId));
    }
}
