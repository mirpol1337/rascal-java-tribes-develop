package VetulusJava.Tribes.Services.BuyService;

import VetulusJava.Tribes.Entities.Building;
import VetulusJava.Tribes.Entities.Troop;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BuyResponse {
    //private Object object;
    private Troop troop;
    private Building building;
    private String message;
    private HttpStatus httpStatus;

    public BuyResponse() {
    }

    public BuyResponse(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public BuyResponse(Troop troop, HttpStatus httpStatus) {
        this.troop = troop;
        this.httpStatus = httpStatus;
    }
}
