package VetulusJava.Tribes.SendGridTest;

import VetulusJava.Tribes.DTOs.CreateUserDto;
import VetulusJava.Tribes.Services.EmailService.ISendGridService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class ISendGridTest {
    @Autowired
    ISendGridService sendGridService;

  // @Test
  // public void testSendMail() throws Exception{
  //     CreateUserDto forCreateEmail = new CreateUserDto("Petra","email@seznam.cz","U královny Petry","password");
  //     String response = sendGridService.sendMail(forCreateEmail);
  //     assertEquals(response.toString(),"Email is sent Successfully!!");
  // }
}
