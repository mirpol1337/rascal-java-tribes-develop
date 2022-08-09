package VetulusJava.Tribes.DTOs;

import jdk.jfr.SettingDefinition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KingdomCredentialsDTO {
    private Long id;
    private String name;
    private int x;
    private int y;

    public KingdomCredentialsDTO(Long id, String name, int x, int y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
    }
}
