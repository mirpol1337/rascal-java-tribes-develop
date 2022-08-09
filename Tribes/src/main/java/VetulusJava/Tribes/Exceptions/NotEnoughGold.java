package VetulusJava.Tribes.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotEnoughGold extends RuntimeException {
    public NotEnoughGold(String errorMessage) {
        super(errorMessage);
    }
}
