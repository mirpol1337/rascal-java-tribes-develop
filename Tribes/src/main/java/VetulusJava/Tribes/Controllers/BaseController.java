package VetulusJava.Tribes.Controllers;

import VetulusJava.Tribes.Exceptions.BadRequestException;
import VetulusJava.Tribes.Exceptions.NotFoundException;
import VetulusJava.Tribes.Security.CurrentUser;
import VetulusJava.Tribes.Services.BuyService.BuyResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class BaseController {
    public UUID getUserId() {
        CurrentUser user = (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.equals(null)? null : user.getId();
    }

    public void checkForException(BuyResponse response) {
        if (response.getHttpStatus() == HttpStatus.BAD_REQUEST) {
            throw new BadRequestException(response.getMessage());
        } else if (response.getHttpStatus() == HttpStatus.NOT_FOUND)
            throw new NotFoundException(response.getMessage());
    }
}
