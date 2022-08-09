package VetulusJava.Tribes.SendGridTest.Helpers;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import springfox.documentation.service.ApiKey;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class ObjectForCreateEmail {
    public String name = "User";
    public String email = "User@TestEmail.cz";
    public String userKingdom = "UserKingdom";

    public ObjectForCreateEmail() {
    }

    public ObjectForCreateEmail(String name, String email, String userKingdom) {
        this.name = name;
        this.email = email;
        this.userKingdom = userKingdom;
    }
}
