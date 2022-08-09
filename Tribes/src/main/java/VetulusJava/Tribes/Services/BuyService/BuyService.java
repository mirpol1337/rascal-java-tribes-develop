package VetulusJava.Tribes.Services.BuyService;

import VetulusJava.Tribes.Entities.Building;
import VetulusJava.Tribes.Entities.Kingdom;
import VetulusJava.Tribes.Entities.Resource;
import VetulusJava.Tribes.Enum.ResourceType;
import VetulusJava.Tribes.Repositories.IBuildingRepository;
import VetulusJava.Tribes.Repositories.IResourceRepository;
import VetulusJava.Tribes.Services.BuildingService.IBuildingService;
import VetulusJava.Tribes.Services.TroopService.ITroopService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static VetulusJava.Tribes.Entities.Constants.*;

@Service
public class BuyService implements IBuyService {
    private IResourceRepository resourceRepository;
    private IBuildingRepository buildingRepository;

    private ITroopService troopService;
    private IBuildingService buildingService;

    public BuyService(IResourceRepository resourceRepository, ITroopService troopService, IBuildingService buildingService, IBuildingRepository buildingRepository) {
        this.resourceRepository = resourceRepository;
        this.troopService = troopService;
        this.buildingService = buildingService;
        this.buildingRepository = buildingRepository;
    }

    @Override
    public BuyResponse buy(BuyRequest buyRequest) {
        Resource resource = resourceRepository.findByTypeAndKingdom(ResourceType.GOLD, buyRequest.getKingdom());
        BuyResponse buyResponse = new BuyResponse();
        switch (buyRequest.getBuyingType()) {
            case TROOP: {
                buyResponse = checkMoney(resource, TROOP_BUY, buyResponse);
                if (buyResponse.getHttpStatus() == HttpStatus.BAD_REQUEST) {
                    return buyResponse;
                }
                buyResponse = troopService.createOrUpdateTroop(null, buyRequest.getKingdom(), buyResponse);
                if (buyResponse.getHttpStatus() == HttpStatus.BAD_REQUEST) {
                    return buyResponse;
                }
                payForNewResources(resource, TROOP_BUY);
                return buyResponse;
            }
            case TRAINTROOP: {
                buyResponse = checkMoney(resource, TROOP_TRAINING_COST, buyResponse);
                if (buyResponse.getHttpStatus() == HttpStatus.BAD_REQUEST) {
                    return buyResponse;
                }
                buyResponse = troopService.createOrUpdateTroop(buyRequest.getLevel(), buyRequest.getKingdom(), buyResponse);
                if (buyResponse.getHttpStatus() == HttpStatus.BAD_REQUEST) {
                    return buyResponse;
                }
                payForNewResources(resource, TROOP_TRAINING_COST);
                return buyResponse;
            }
            case UPGRADEBUILDING: {
                var amount = BUILDING_UPGRADE_ONE_BUILDING * buyRequest.getLevel();
                buyResponse = checkLevel(buyRequest.getBuildingId(), buyRequest.getLevel(), buyRequest.getKingdom(), buyResponse);
                if (buyResponse.getHttpStatus() == HttpStatus.BAD_REQUEST) {
                    return buyResponse;
                }
                buyResponse = checkMoney(resource, amount, buyResponse);
                if (buyResponse.getHttpStatus() == HttpStatus.BAD_REQUEST) {
                    return buyResponse;
                }
                buyResponse = buildingService.upgradeByID(buyRequest.getBuildingId(), buyRequest.getLevel(), buyRequest.getKingdom(), buyResponse);
                if (buyResponse.getHttpStatus() == HttpStatus.BAD_REQUEST) {
                    return buyResponse;
                }
                payForNewResources(resource, amount);
                return buyResponse;
            }
            default:
                return buyResponse;
        }

    }

    private BuyResponse checkLevel(long buildingId, Integer level, Kingdom kingdom, BuyResponse buyResponse) {
        Building buildingLevel = buildingRepository.findByIDAndAndKingdom(buildingId, kingdom);
        if (buildingLevel.getLevel() == level) {
            buyResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
            buyResponse.setMessage("You are already on this level!");
            return buyResponse;
        } else if (buildingLevel.getLevel() > level) {
            buyResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
            buyResponse.setMessage("You cannot put down your level!");
            return buyResponse;
        } else if (buildingLevel.getLevel() < level && buildingLevel.getLevel() != (level - 1)) {
            buyResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
            buyResponse.setMessage("You cannot upgrade your building more than +1 level");
            return buyResponse;
        }
        return buyResponse;
    }

    private BuyResponse checkMoney(Resource resource, int amount, BuyResponse buyResponse) {
        if (resource.getAmount() < amount) {
            buyResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
            buyResponse.setMessage("Not enough gold!");
            return buyResponse;
        }
        return buyResponse;
    }

    private void payForNewResources(Resource resource, int amount) {
        resource.setAmount(resource.getAmount() - amount);
        resourceRepository.save(resource);
    }
}

