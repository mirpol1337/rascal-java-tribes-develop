package VetulusJava.Tribes.DTOs;

import VetulusJava.Tribes.Entities.Troop;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class TroopsDTO {
    Collection<Troop> troops;
}
