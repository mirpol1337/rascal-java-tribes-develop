package VetulusJava.Tribes.DTOs;

import VetulusJava.Tribes.Validation.ValidEmail;
import VetulusJava.Tribes.Validation.ValidPassword;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.*;

@Getter
@Setter
public class CreateUserDto {
    @NotBlank(message = "Username is required")
    @ApiModelProperty(notes = "User Name", required = true)
    private String name;

    @ValidEmail(message = "Valid Email is required")
    @ApiModelProperty(notes = "Email like eamil@eamil.cz", required = true)
    private String email;

    @ApiModelProperty(notes = "Kingdom", required = false)
    private String userKingdom;

    @NotBlank(message = "Password is required.")
    @ValidPassword
    @ApiModelProperty(notes = "Password is required", required = true)
    private String password;

    public CreateUserDto() {
    }

    public CreateUserDto(String name, String email, String userKingdom, String password) {
        this.name = name;
        this.email = email;
        this.userKingdom = userKingdom;
        this.password = password;
    }

    public CreateUserDto(String name, String email,String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
