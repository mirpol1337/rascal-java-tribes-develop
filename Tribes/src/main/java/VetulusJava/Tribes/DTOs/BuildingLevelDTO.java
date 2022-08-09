package VetulusJava.Tribes.DTOs;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class BuildingLevelDTO {
    @Min(value = 1,message = "Invalid building level!")
    @Max(value = 20, message = "Maximum level is 20")
    @NotNull(message = "Missing parameter(s): type!")
    private Integer level;

    public BuildingLevelDTO() {

    }

    public BuildingLevelDTO(int level){
        this.level = level;
    }
}


