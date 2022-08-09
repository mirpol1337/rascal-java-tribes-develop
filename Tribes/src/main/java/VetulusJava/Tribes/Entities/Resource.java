package VetulusJava.Tribes.Entities;

import VetulusJava.Tribes.Enum.ResourceType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long ID;
    @Enumerated(EnumType.STRING)
    public ResourceType type;
    public int amount;
    public int generation;
    @JsonIgnore
    @ManyToOne
    public Kingdom kingdom;

    public Resource() {
    }

    public Resource(ResourceType type, int amount, int generation, Kingdom kingdom) {
        this.type = type;
        this.amount = amount;
        this.generation = generation;
        this.kingdom = kingdom;
    }

    public void generateResources(int storage) {
        if (amount + generation >= storage) {
            amount = storage;
        } else {
            amount = amount + generation;
        }
    }
}
