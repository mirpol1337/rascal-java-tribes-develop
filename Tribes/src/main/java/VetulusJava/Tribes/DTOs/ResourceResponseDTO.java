package VetulusJava.Tribes.DTOs;

import VetulusJava.Tribes.Enum.ResourceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceResponseDTO {
    private ResourceType type;
    private int amount;
    private int generation;

    public ResourceResponseDTO(ResourceType type, int amount, int generation){
        this.type = type;
        this.amount = amount;
        this.generation = generation;
    }
}
