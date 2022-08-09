package VetulusJava.Tribes.Services.BuyService;

import VetulusJava.Tribes.Entities.Kingdom;
import VetulusJava.Tribes.Enum.BuildingType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyRequest {
    private BuyingType buyingType;
    private Kingdom kingdom;
    private Integer level;
    private BuildingType buildingType;
    private long buildingId;

    // Create troop
    public BuyRequest(Kingdom kingdom, BuyingType buyingType) {
        this.kingdom = kingdom;
        this.buyingType = buyingType;
    }


    // Train troop
    public BuyRequest(Kingdom kingdom, BuyingType buyingType, int level) {
        this.kingdom = kingdom;
        this.buyingType = buyingType;
        this.level = level;
    }

    // Upgrade Building
    public BuyRequest(Kingdom kingdom, BuyingType buyingType,  Integer level, long buildingId) {
        this.kingdom = kingdom;
        this.buyingType = buyingType;
        this.level = level;
        this.buildingId = buildingId;
    }
}
