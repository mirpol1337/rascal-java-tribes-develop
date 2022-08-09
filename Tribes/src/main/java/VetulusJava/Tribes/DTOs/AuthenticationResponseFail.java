package VetulusJava.Tribes.DTOs;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthenticationResponseFail {

    private String status;
    private String message;

    public AuthenticationResponseFail(String message) {
        this.status = "error";
        this.message = message;
    }
}


