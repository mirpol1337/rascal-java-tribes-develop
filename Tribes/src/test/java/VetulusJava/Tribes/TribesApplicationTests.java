package VetulusJava.Tribes;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;


@SpringBootTest
@AutoConfigureTestDatabase
@TestPropertySource(
		locations = "classpath:application-test.properties")
class TribesApplicationTests {


	@Test
	void contextLoads() {

	}

}
