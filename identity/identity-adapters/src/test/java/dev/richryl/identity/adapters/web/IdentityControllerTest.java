package dev.richryl.identity.adapters.web;

import dev.richryl.identity.application.ports.dto.UserInfoResponse;
import dev.richryl.identity.application.ports.in.RetrieveUserInfoUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = IdentityController.class
)
@ContextConfiguration(classes = {IdentityController.class})
public class IdentityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RetrieveUserInfoUseCase retrieveUserInfoUseCase;
    private static final String id = "f47ac10b-58cc-4372-a567-0e02b2c3d479";

    @Test
    @DisplayName("Should return user identity information when requested to /api/identity/me")
    @WithMockUser(value = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    void shouldReturnUserIdentityInformation() throws Exception {

        when(retrieveUserInfoUseCase.handle(UUID.fromString(id)))
                .thenReturn(new UserInfoResponse(
                        UUID.randomUUID(),
                        "user-123"
                        ));

        mockMvc.perform(get("/api/identity/me"))
                .andExpect(
                        status().isOk()
                ).andExpect(jsonPath("$.id").isNotEmpty());

        verify(retrieveUserInfoUseCase, times(1)).handle(UUID.fromString(id));
    }
}
