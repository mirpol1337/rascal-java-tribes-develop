package VetulusJava.Tribes.PersistenceTest;

import VetulusJava.Tribes.Entities.ApplicationUser;
import VetulusJava.Tribes.Repositories.IUserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase
@TestPropertySource(
        locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserRepositoryTest {

    @Autowired
    IUserRepository userRepository;

    @BeforeAll
    private void init(){
        userRepository.save(new ApplicationUser("Bond", "pass"));
    }

    @Test   // why is it called saveUser?
    public void saveUser() {
        Iterable<ApplicationUser> user = userRepository.findAll();
        assertThat(user).extracting(ApplicationUser::getName).contains("Bond");
    }

    @Test
    public void findUser() {
        userRepository.save(new ApplicationUser("jana", "password", "petra@seznam.cz"));
        ApplicationUser actual = userRepository.findByName("jana");
        assertThat(actual.getName()).isEqualTo("jana");
    }

    @Test
    public void emailExist() {
        userRepository.save(new ApplicationUser("Hana", "password", "petra@seznm.cz"));
        ApplicationUser actual = userRepository.findByEmail("petra@seznm.cz");
        assertThat(actual.getEmail()).isEqualTo("petra@seznm.cz");
    }

    @Test
    public void emailDoesntExist() {
        userRepository.save(new ApplicationUser("name", "password", "petra@seznam.cz"));
        ApplicationUser actual = userRepository.findByEmail("pet@seznam.cz");
        assertThat(actual).isEqualTo(null);
    }

    @Test
    public void createNewUser(){
        ApplicationUser user = new ApplicationUser("Katharina","password1","emol@email.cz");
        user.setId(UUID.fromString("529093c7-ae8a-447a-a3ce-03c5b8f1e4a9"));
        userRepository.save(user);
        ApplicationUser actual = userRepository.findByName("Katharina");
        assertThat(actual.getName()).isEqualTo("Katharina");
    }
}