package dev.richryl.identity.adapters.web;

import dev.richryl.identity.application.ports.dto.UserInfoResponse;
import dev.richryl.identity.application.ports.in.RetrieveUserInfoUseCase;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/identity")
public class IdentityController {

    private final RetrieveUserInfoUseCase retrieveUserInfoUseCase;

    public IdentityController(RetrieveUserInfoUseCase retrieveUserInfoUseCase) {
        this.retrieveUserInfoUseCase = retrieveUserInfoUseCase;
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> retrieveUserInfo(
            Principal principal
    ) {
        String id = principal.getName();
        UserInfoResponse userInfo = retrieveUserInfoUseCase.handle(UUID.fromString(id));
        return ResponseEntity.ok(userInfo);
    }
}
