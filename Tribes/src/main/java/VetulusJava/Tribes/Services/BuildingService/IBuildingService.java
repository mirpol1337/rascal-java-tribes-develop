package VetulusJava.Tribes.Services.BuildingService;

import VetulusJava.Tribes.Entities.Building;
import VetulusJava.Tribes.Entities.Kingdom;
import VetulusJava.Tribes.Enum.BuildingType;
import VetulusJava.Tribes.Services.BuyService.BuyResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public interface IBuildingService {
    Building getById(long ID);

    BuyResponse upgradeByID(long buildingId, int level, Kingdom kingdom, BuyResponse buyResponse);

    Building createByType(BuildingType type, Kingdom kingdom);

    void setStartingBuildings(Kingdom kingdom);

    Collection<Building>findByKingdom(Kingdom kingdom);

    Building getByTypeAndKingdomId(BuildingType type, Long kingdomId);

    void decreaseBuildingLevel(long kingdomId);
}