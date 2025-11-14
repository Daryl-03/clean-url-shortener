package dev.richryl.identity.adapters.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import dev.richryl.identity.application.ports.in.RetrieveUserInfoUseCase;
import dev.richryl.identity.application.ports.dto.UserInfoResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = IdentityController.class
)
@ContextConfiguration(classes = {IdentityController.class})
public class IdentityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RetrieveUserInfoUseCase retrieveUserInfoUseCase;

    @Test
    @DisplayName("Should return user identity information when requested to /api/identity/me")
    @WithMockUser(value = "user-123")
    void shouldReturnUserIdentityInformation() throws Exception {
        String externalId = "user-123";
        when(retrieveUserInfoUseCase.handle(externalId))
                .thenReturn(new UserInfoResponse(
                        UUID.randomUUID()
                        ));

        mockMvc.perform(get("/api/identity/me"))
                .andExpect(
                        status().isOk()
                ).andExpect(jsonPath("$.id").isNotEmpty());

        verify(retrieveUserInfoUseCase, times(1)).handle(externalId);
    }
}
