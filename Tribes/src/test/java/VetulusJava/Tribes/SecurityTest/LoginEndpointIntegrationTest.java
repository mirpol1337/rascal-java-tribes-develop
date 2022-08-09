package VetulusJava.Tribes.SecurityTest;

import VetulusJava.Tribes.Services.SeedingService.InitializeDB;
import VetulusJava.Tribes.Controllers.UserController;
import VetulusJava.Tribes.DTOs.AuthenticationRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@TestPropertySource(
        locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginEndpointIntegrationTest {
    @Autowired
    UserController userController;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    InitializeDB initializeDB;

    private final AuthenticationRequest validUser = new AuthenticationRequest("Bond", "123");
    private final AuthenticationRequest invalidNameUser = new AuthenticationRequest("Pepa", "123");
    private final AuthenticationRequest invalidPasswordUser = new AuthenticationRequest("Bond", "fdssa");
    private final AuthenticationRequest missingPasswordUser = new AuthenticationRequest("Bond", "");
    private final AuthenticationRequest missingLoginUser = new AuthenticationRequest("", "fdssa");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public void init() {
        initializeDB.seedDB();
    }

    @Test
    @DisplayName("valid user")
    void validUser() throws Exception {
        mockMvc.perform(post("/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("ok"));
    }

    @Test
    @DisplayName("missing login")
    public void missingLogin() throws Exception {
        mockMvc.perform(post("/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(missingLoginUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"));
    }

    @Test
    @DisplayName("missing password")
    public void missingPassword() throws Exception {
        mockMvc.perform(post("/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(missingPasswordUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"));
    }

    @Test
    @DisplayName("invalid name")
    public void invalidLogin() throws Exception {
        mockMvc.perform(post("/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(invalidNameUser)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("error"));
    }

    @Test
    @DisplayName("invalid password")
    public void invalidPassword() throws Exception {
        mockMvc.perform(post("/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(invalidPasswordUser)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("error"));
    }
}
