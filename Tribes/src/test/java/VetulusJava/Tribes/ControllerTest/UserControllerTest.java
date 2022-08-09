package VetulusJava.Tribes.ControllerTest;

import VetulusJava.Tribes.Controllers.UserController;
import VetulusJava.Tribes.DTOs.CreateUserDto;
import VetulusJava.Tribes.Entities.ApplicationUser;
import VetulusJava.Tribes.Repositories.IUserRepository;
import VetulusJava.Tribes.Security.JwtUtil;
import VetulusJava.Tribes.Security.UserDetailsServiceImpl;
import VetulusJava.Tribes.Services.EmailService.ISendGridService;
import VetulusJava.Tribes.Services.KingdomService.IKingdomService;
import VetulusJava.Tribes.Services.LoggingService.ILoggingService;
import VetulusJava.Tribes.Services.UserService.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("User controller")
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @MockBean
    ILoggingService loggingService;
    @MockBean
    private IUserRepository userRepository;
    @MockBean
    private ISendGridService sendGridService;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private IUserService userService;
    @MockBean
    private IKingdomService kingdomService;
    @Autowired
    UserController userController;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


   // @Test
   // @DisplayName("Register user new user")
   // public void whenPostRequestToUsersAndValidUser_thenCorrectResponse() throws Exception {
   //     CreateUserDto user = new CreateUserDto("name","password@seznam.cz","","password1" );
   //     mockMvc.perform(post("/register")
   //             .contentType("application/json")
   //             .content(objectMapper.writeValueAsString(user)))
   //            .andExpect(status().isOk());
   // }

    @Test
    @DisplayName("Register user missing username")
    public void userTryRegisterWithoutName() throws Exception {
        String user = "{\"name\": \"\", \"email\" : \"bob@domain.com\",\"password\":\"password8\"}";
        mockMvc.perform(post("/register")
                .content(user)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.errors", hasItem("Username is required")));
        verify(userRepository, times(0)).save(any(ApplicationUser.class));
    }

    @Test
    @DisplayName("Register user missing email")
    public void userTryRegisterWithoutEmail() throws Exception {
        String user = "{\"name\": \"petra\", \"email\" : \"\",\"password\":\"Password8\"}";
        mockMvc.perform(post("/register")
                .content(user)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasItem("Valid Email is required")));
        verify(userRepository, times(0)).save(any(ApplicationUser.class));
    }

    @Test
    @DisplayName("Register user use bad mail")
    public void userTryRegisterWithWrongEmail() throws Exception {
        String user = "{\"name\": \"petra\", \"email\" : \"email.cz\",\"password\":\"Password8\"}";
        mockMvc.perform(post("/register")
                .content(user)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasItem("Valid Email is required")));
        verify(userRepository, times(0)).save(any(ApplicationUser.class));
    }

    @Test
    @DisplayName("Register user dont use password")
    public void whenPostRequestToUsersAndInvalidPassword_thenCorrectResponse() throws Exception {
        String user = "{\"name\": \"petra\", \"email\" : \"email@seznam.cz\",\"password\":\"\"}";
        mockMvc.perform(post("/register")
                .content(user)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", hasItem("Password is required.")));
        verify(userRepository, times(0)).save(any(ApplicationUser.class));
    }

}

