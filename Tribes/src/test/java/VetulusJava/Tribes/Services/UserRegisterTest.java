package VetulusJava.Tribes.Services;

import VetulusJava.Tribes.Exceptions.UserAlreadyExistException;
import VetulusJava.Tribes.DTOs.CreateUserDto;
import VetulusJava.Tribes.Entities.ApplicationUser;
import VetulusJava.Tribes.Repositories.IUserRepository;
import VetulusJava.Tribes.Services.EmailService.ISendGridService;
import VetulusJava.Tribes.Services.KingdomService.IKingdomService;
import VetulusJava.Tribes.Services.UserService.IUserService;
import VetulusJava.Tribes.Services.UserService.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class UserRegisterTest {
    @Autowired
    private IUserService userService;
    @Autowired
    ISendGridService sendgrind;
    @Autowired
    IKingdomService kingdomServis;

    private IUserRepository userRepository = mock(IUserRepository.class);

    private static ApplicationUser user1 = new ApplicationUser("petra", "petra@seznam.cz", "petra@seznam.cz");
    private static CreateUserDto user2 = new CreateUserDto("petr", "petra@seznam.cz", "pieretin", "password1");
    private static ApplicationUser user3 = new ApplicationUser("petra", "email@seznam.cz", "password1");
    private static CreateUserDto user4 = new CreateUserDto("petra", "petra@seznam.cz", "pieretin", "password1");

    @BeforeEach
    void init() {
        userService = new UserService(userRepository, sendgrind, kingdomServis);
    }

    @Test
    public void findExistUser() {
        when(userRepository.findByName("petra")).thenReturn(user1);
        ApplicationUser actual = userService.getUser("petra");
        assertEquals("petra", actual.getName());
    }

    @Test
    public void whenExceptionThrown_userEmailAlreadyUsed() {
        when(userRepository.findByEmail(user1.getEmail())).thenReturn(user1);
        UserAlreadyExistException exception = assertThrows(UserAlreadyExistException.class, () -> {
            userService.registerNewUserAccount(user2, user2.getPassword());
        });
        String expectedMessage = "There is an account with that email address: petra@seznam.cz";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void whenExceptionThrown_userNameAlreadyUsed() {
        when(userRepository.findByName(user3.getName())).thenReturn(user3);
        UserAlreadyExistException exception = assertThrows(UserAlreadyExistException.class, () -> {
            userService.registerNewUserAccount(user4, user4.getPassword());
        });
        String expectedMessage = "There is an account with that name petra";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
