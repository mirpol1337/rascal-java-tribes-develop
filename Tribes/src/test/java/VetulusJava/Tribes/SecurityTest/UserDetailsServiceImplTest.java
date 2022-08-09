package VetulusJava.Tribes.SecurityTest;

import VetulusJava.Tribes.Entities.ApplicationUser;
import VetulusJava.Tribes.Security.UserDetailsServiceImpl;
import VetulusJava.Tribes.Services.UserService.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserDetailsServiceImplTest {

    @Autowired
    private MockMvc mvc;

    IUserService userService = mock(IUserService.class);
    ApplicationUser applicationUser = new ApplicationUser("Bond",  "123");
    UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userDetailsService = new UserDetailsServiceImpl(userService);
    }

    @Test
    public void userFound() {
        when(userService.getUser("Bond")).thenReturn(applicationUser);
        UserDetails actual = userDetailsService.loadUserByUsername("Bond");
        assertEquals("Bond", actual.getUsername());
        assertEquals("123", actual.getPassword());
    }

    @Test
    public void userNotFound() {
        when(userService.getUser("Pepa")).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername("Pepa"));
    }
}
