package VetulusJava.Tribes.DTOs;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthenticationResponseOk {
    private String status;
    private String jwt;

    public AuthenticationResponseOk(String jwt) {
        this.status = "ok";
        this.jwt = jwt;
    }
}
