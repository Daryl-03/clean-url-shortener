package dev.richryl.bootstrap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ShortlinkApp.class)
@ActiveProfiles("local")
@AutoConfigureMockMvc
class CorsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void   shouldReturnCorrectCorsHeadersForLocalProfile() throws Exception {
        mockMvc.perform(options("/api/shortlinks")
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
                .andExpect(header().string("Access-Control-Allow-Methods", "POST,GET,PUT,DELETE"));
    }


    @Test
    void shouldReturnForbiddenForOtherOriginsOnLocal() throws Exception {
        mockMvc.perform(options("/api/shortlinks")
                        .header("Origin", "http://malicious-site.com")
                        .header("Access-Control-Request-Method", "POST"))
                .andExpect(status().isForbidden());
    }
}
