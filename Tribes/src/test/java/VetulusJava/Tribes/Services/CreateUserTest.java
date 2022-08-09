package VetulusJava.Tribes.Services;

import VetulusJava.Tribes.DTOs.CreateUserDto;
import VetulusJava.Tribes.Entities.ApplicationUser;
import VetulusJava.Tribes.Services.UserService.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class CreateUserTest {
    @Autowired
    IUserService userService;

    private static CreateUserDto user7 = new CreateUserDto("jana", "jana@seznam.cz", "", "password");
    private static CreateUserDto user5 = new CreateUserDto("Hana", "hana@seznam.cz", "pieretin", "password1");
    private static CreateUserDto user8 = new CreateUserDto("panna", "pana@seznam.cz", "password");

    @Test
    public void userEmptyKingdomName() {
        ApplicationUser actual = userService.registerNewUserAccount(user7, user7.getPassword());
        assertEquals("jana", actual.getKingdom().getName());
    }

    @Test
    public void userNameNotUsed_RegisterNewUser() {
        ApplicationUser actual = userService.registerNewUserAccount(user5, user5.getPassword());
        assertEquals("pieretin", actual.getKingdom().getName());
    }

    @Test
    public void userNotUseKingdomName() {
        ApplicationUser actual = userService.registerNewUserAccount(user8, user8.getPassword());
        assertEquals("panna", actual.getKingdom().getName());
    }
}
