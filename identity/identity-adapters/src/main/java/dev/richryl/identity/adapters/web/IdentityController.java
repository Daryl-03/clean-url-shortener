package dev.richryl.identity.adapters.web;

import dev.richryl.identity.application.ports.dto.UserInfoResponse;
import dev.richryl.identity.application.ports.in.RetrieveUserInfoUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("api/identity")
public class IdentityController {

    private final RetrieveUserInfoUseCase retrieveUserInfoUseCase;

    public IdentityController(RetrieveUserInfoUseCase retrieveUserInfoUseCase) {
        this.retrieveUserInfoUseCase = retrieveUserInfoUseCase;
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> retrieveUserInfo(
            Principal principal
    ) {
        String externalId = principal.getName();
        UserInfoResponse userInfo = retrieveUserInfoUseCase.handle(externalId);
        return ResponseEntity.ok(userInfo);
    }
}
