package VetulusJava.Tribes.Services.UserService;

import VetulusJava.Tribes.Entities.Constants;
import VetulusJava.Tribes.Entities.Kingdom;
import VetulusJava.Tribes.Exceptions.UserAlreadyExistException;
import VetulusJava.Tribes.DTOs.CreateUserDto;
import VetulusJava.Tribes.Entities.ApplicationUser;
import VetulusJava.Tribes.Repositories.IUserRepository;
import VetulusJava.Tribes.Services.EmailService.ISendGridService;
import VetulusJava.Tribes.Services.KingdomService.IKingdomService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService implements IUserService {

    private IUserRepository userRepository;
    private ISendGridService sendGridService;
    private IKingdomService kingdomService;

    public UserService(IUserRepository userRepository, ISendGridService sendGridService, IKingdomService kingdomService) {
        this.userRepository = userRepository;
        this.sendGridService = sendGridService;
        this.kingdomService = kingdomService;
    }

    public ApplicationUser getUser(String userName) {
        return userRepository.findByName(userName);
    }

    @Transactional
    @Override
    public ApplicationUser registerNewUserAccount(CreateUserDto userDto, String password) {

        if (emailExist(userDto.getEmail())) {
             throw new UserAlreadyExistException(
                   "There is an account with that email address: " + userDto.getEmail());
        } else if (userExist(userDto.getName())) {
            throw new UserAlreadyExistException(
                    "There is an account with that name " +
                            userDto.getName());
        } else {
            ApplicationUser newUser = new ApplicationUser();
            Kingdom newKingdom = createUserKindgdom(userDto);

            newUser.setEmail(userDto.getEmail());
            newUser.setPassword(password);
            newUser.setName(userDto.getName());
            //add new kingdom
            newUser.setKingdom(newKingdom);
            //add user to kingdom
            newKingdom.setUser(newUser);

            //sendig email
            sendRegisterEmail(userDto);
            //save user
            newKingdom = kingdomService.setKingdomLocation(newKingdom, Constants.DEFAULT_SPREAD_RANGE);
            userRepository.save(newUser);
            kingdomService.saveKingdom(newKingdom);
            kingdomService.setFullKingdom(newKingdom);
            return newUser;
        }
    }


    private Kingdom createUserKindgdom(CreateUserDto userDto) {
        Kingdom newKingdom = new Kingdom();
        if (userDto.getUserKingdom() == null || userDto.getUserKingdom().isEmpty() || userDto.getUserKingdom().isBlank() || userDto.getUserKingdom().equals("string")) {
            newKingdom.setName(userDto.getName());

        } else
            newKingdom.setName(userDto.getUserKingdom());
        return newKingdom;
    }

    @Transactional
    private boolean emailExist(String mail) {
        ApplicationUser email = userRepository.findByEmail(mail);
        return email == null ? false : true;
    }


    @Transactional
    private boolean userExist(String name) {
        ApplicationUser user = userRepository.findByName(name);
        return user == null ? false : true;
    }

    private void sendRegisterEmail(CreateUserDto createUserDto) {
        sendGridService.sendMail(createUserDto);
    }
}
