package VetulusJava.Tribes.Services.BuildingService;

import VetulusJava.Tribes.Entities.Building;
import VetulusJava.Tribes.Entities.Kingdom;
import VetulusJava.Tribes.Entities.Troop;
import VetulusJava.Tribes.Enum.BuildingType;
import VetulusJava.Tribes.Exceptions.BadRequestException;
import VetulusJava.Tribes.Exceptions.NotFoundException;
import VetulusJava.Tribes.Repositories.IBuildingRepository;
import VetulusJava.Tribes.Services.BuyService.BuyResponse;
import VetulusJava.Tribes.Services.KingdomService.KingdomService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

import static VetulusJava.Tribes.Entities.Constants.BUILDING_CONSTRUCTION_TIME;
import static VetulusJava.Tribes.Entities.Constants.MAX_BUILDING_LEVEL;


@Service
@Qualifier("BuildingService")
public class BuildingService implements IBuildingService {
    private IBuildingRepository buildingRepository;

    public BuildingService(IBuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    public Building getById(long ID) {
        return buildingRepository.findById(ID).orElse(null);
    }

    public Collection<Building> findByKingdom(Kingdom kingdom) {
        Collection result = new ArrayList<>();
        result.add(buildingRepository.findBuildingByKingdom(kingdom));
        return result;
    }

    @Override
    public Building createByType(BuildingType type, Kingdom kingdom) {
        var newBuilding = new Building(type, 1, 1, (System.currentTimeMillis() / 1000), System.currentTimeMillis() / 1000, kingdom);
        buildingRepository.save(newBuilding);
        return newBuilding;
    }

    @Override
    public void setStartingBuildings(Kingdom kingdom) {
        for (var item : BuildingType.values()) {
            createByType(item, kingdom);
        }
    }

    @Override
    public BuyResponse upgradeByID(long buildingId, int level, Kingdom kingdom, BuyResponse buyResponse) {
        var building = buildingRepository.findById(buildingId).get();
        buyResponse = canBeUpgraded(buildingId, level, kingdom, buyResponse);
        if (buyResponse.getHttpStatus() == HttpStatus.BAD_REQUEST) {
            return buyResponse;
        }
        long currentTime = System.currentTimeMillis() / 1000;
        if (buildingInBuilProcess(kingdom, currentTime)) {
            building.setLevel(level);
            building.setStarted_at(currentTime);
            building.setFinished_at(currentTime + (BUILDING_CONSTRUCTION_TIME * level));
            buyResponse.setBuilding(building);
            buyResponse.setHttpStatus(HttpStatus.OK);
            return buyResponse;
        } else
            buyResponse.setMessage("You already upgrade one of your building");
            buyResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
            return buyResponse;
    }


    private boolean buildingInBuilProcess(Kingdom kingdom, long curretnTime) {
       Collection<Building> building =buildingRepository.findBuildingsByFinishedAtGreaterThanAndKingdom(kingdom,curretnTime);
        if (building.isEmpty()) {
            return true;
        } else
            return false;
    }

    private BuyResponse canBeUpgraded(long buildingId, int level, Kingdom kingdom, BuyResponse buyResponse) {
        var townHall = getByTypeAndKingdomId(BuildingType.TOWNHALL, kingdom.getId());
        var buildingToUpgrade = getById(buildingId);
        if (buildingId == townHall.getID() && level <= MAX_BUILDING_LEVEL) {
            return buyResponse;
        }
        if (townHall.level == MAX_BUILDING_LEVEL && level > MAX_BUILDING_LEVEL && buildingToUpgrade.level == MAX_BUILDING_LEVEL) {
            buyResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
            buyResponse.setMessage("You have reached maximum level: 20!");
            return buyResponse;
        }
        if (townHall.level < level) {
            buyResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
            buyResponse.setMessage("You cannot upgrade to this level, upgrade your Town Hall first!");
            return buyResponse;
        }
        return buyResponse;
    }

    @Override
    public Building getByTypeAndKingdomId(BuildingType type, Long kingdomId) {
        var building = buildingRepository.findByTypeAndKingdomId(type, kingdomId);
        if (building.isPresent()) {
            return building.get();
        } else {
            throw new NotFoundException("You don't have the required building");
        }
    }

    @Override
    public void decreaseBuildingLevel(long kingdomId) {
        ArrayList<Building> destroyableBuildings = new ArrayList();
        for (var type: BuildingType.values()) {
            Building building = getByTypeAndKingdomId(type, kingdomId);
            if (building.getLevel()>1) {
                destroyableBuildings.add(building);
            }
        }
        if (!destroyableBuildings.isEmpty()){
            Random rng = new Random();
            Building randomBuilding = (Building) destroyableBuildings.toArray()[rng.nextInt(destroyableBuildings.size())];
            randomBuilding.setLevel(randomBuilding.getLevel()-1);
            randomBuilding.setFinished_at(System.currentTimeMillis()/1000);
            buildingRepository.save(randomBuilding);
            System.out.println(randomBuilding.type + "'s level decreased to " + randomBuilding.getLevel());
        }
    }
}