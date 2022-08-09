package VetulusJava.Tribes.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Time {
    @Id
    @GeneratedValue
    public long id;
    public long ticks;
    public long serverStart;
}
