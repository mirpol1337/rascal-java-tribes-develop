package VetulusJava.Tribes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;

@SpringBootApplication
@EnableSwagger2
public class TribesApplication {

    public static void main(String[] args) {
        SpringApplication.run(TribesApplication.class, args);
    }
}
