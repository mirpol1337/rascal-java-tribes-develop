package VetulusJava.Tribes.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UserDto {
    private String id;
    private String name;
    private String kingdomId;

    public UserDto() {
    }

    public UserDto(String id, String name, String kingdomId) {
        this.id = id;
        this.name = name;
        this.kingdomId = kingdomId;
    }
}
