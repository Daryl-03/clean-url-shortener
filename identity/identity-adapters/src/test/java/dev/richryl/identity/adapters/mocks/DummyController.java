package dev.richryl.identity.adapters.mocks;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DummyController {

    @GetMapping("/api/test-route")
    public String dummyEndpoint() {
        return "ok";
    }
}
