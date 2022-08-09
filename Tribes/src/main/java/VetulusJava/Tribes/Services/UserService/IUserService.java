package VetulusJava.Tribes.Services.UserService;

import VetulusJava.Tribes.Exceptions.UserAlreadyExistException;
import VetulusJava.Tribes.DTOs.CreateUserDto;
import VetulusJava.Tribes.Entities.ApplicationUser;

public interface IUserService {
    public ApplicationUser getUser(String userName);
    public ApplicationUser registerNewUserAccount(CreateUserDto userDto, String password) throws UserAlreadyExistException;
}
