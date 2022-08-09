package VetulusJava.Tribes.DTOs;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class PutLevelDto {
    @NotBlank(message = "Missing parameter(s): type!")
    @Min(value = 1,message = "Invalid troop level!")
    private Integer level;

    public PutLevelDto(Integer level) {
        this.level = level;
    }
}
