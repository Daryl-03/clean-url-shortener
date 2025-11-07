package dev.richryl.shortlink.adapters.web;

import dev.richryl.shortlink.Shortlink;
import dev.richryl.shortlink.adapters.web.dto.CreateShortlinkRequest;
import dev.richryl.shortlink.adapters.web.dto.UpdateShortlinkRequest;
import dev.richryl.shortlink.application.ports.in.CreateShortlinkUseCase;
import dev.richryl.shortlink.application.ports.in.DeleteShortlinkByIdUseCase;
import dev.richryl.shortlink.application.ports.in.GetShortlinkByIdUseCase;
import dev.richryl.shortlink.application.ports.in.UpdateShortlinkByIdUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/shortlinks")
public class ShortlinkController {
    private final CreateShortlinkUseCase createShortlinkUseCase;
    private final GetShortlinkByIdUseCase getShortlinkByIdUseCase;
    private final DeleteShortlinkByIdUseCase deleteShortlinkByIdUseCase;
    private final UpdateShortlinkByIdUseCase updateShortlinkByIdUseCase;

    public ShortlinkController(CreateShortlinkUseCase createShortlinkUseCase, GetShortlinkByIdUseCase getShortlinkByIdUseCase, DeleteShortlinkByIdUseCase deleteShortlinkByIdUseCase, UpdateShortlinkByIdUseCase updateShortlinkByIdUseCase) {
        this.createShortlinkUseCase = createShortlinkUseCase;
        this.getShortlinkByIdUseCase = getShortlinkByIdUseCase;
        this.deleteShortlinkByIdUseCase = deleteShortlinkByIdUseCase;
        this.updateShortlinkByIdUseCase = updateShortlinkByIdUseCase;
    }

    @PostMapping()
    public ResponseEntity<Shortlink> createShortlink(@RequestBody @Valid CreateShortlinkRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(createShortlinkUseCase.handle(request.url()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shortlink> getShortlink(@PathVariable("id") UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(getShortlinkByIdUseCase.handle(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShortlink(@PathVariable("id") UUID id) {
        deleteShortlinkByIdUseCase.handle(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping()
    public ResponseEntity<Shortlink> updateShortlink(@RequestBody @Valid UpdateShortlinkRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(updateShortlinkByIdUseCase.handle(request.toCommand()));
    }

}
