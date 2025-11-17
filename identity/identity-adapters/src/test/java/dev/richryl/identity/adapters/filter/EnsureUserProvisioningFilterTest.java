package dev.richryl.identity.adapters.filter;

import dev.richryl.identity.adapters.mocks.DummyController;
import dev.richryl.identity.application.ports.in.CreateUserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
        controllers = DummyController.class
)
@ContextConfiguration(classes = {DummyController.class, EnsureUserProvisioningFilter.class})
@AutoConfigureMockMvc
public class EnsureUserProvisioningFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateUserUseCase createUserUseCase;

//    @Test
//    @DisplayName("Should call user creation use case with correct external ID from request auth")
//    @WithMockUser(value = "valid")
//    void shouldCallUserCreationUseCaseWithCorrectExternalId() throws Exception {
//        doNothing().when(createUserUseCase).handle(anyString());
//
//        mockMvc.perform(get("/api/test-route")
//                        .header("Authorization", "Bearer valid")
//                )
//                .andExpect(status().isOk());
//
//        verify(createUserUseCase, times(1)).handle("valid");
//    }
}
