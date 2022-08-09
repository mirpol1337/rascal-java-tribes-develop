package VetulusJava.Tribes.Services.EmailService;

import VetulusJava.Tribes.DTOs.CreateUserDto;

public interface ISendGridService {
    String sendMail(CreateUserDto createUserDto);
}
