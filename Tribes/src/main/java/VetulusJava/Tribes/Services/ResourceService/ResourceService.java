package VetulusJava.Tribes.Services.ResourceService;

import VetulusJava.Tribes.Entities.Kingdom;
import VetulusJava.Tribes.Entities.Resource;
import VetulusJava.Tribes.Enum.BuildingType;
import VetulusJava.Tribes.Enum.ResourceType;
import VetulusJava.Tribes.Repositories.IBuildingRepository;
import VetulusJava.Tribes.Repositories.IKingdomRepository;
import VetulusJava.Tribes.Repositories.IResourceRepository;
import VetulusJava.Tribes.Services.BuildingService.IBuildingService;
import VetulusJava.Tribes.Services.TroopService.ITroopService;
import org.springframework.stereotype.Service;

import static VetulusJava.Tribes.Entities.Constants.TROOP_FOOD_USAGE;

@Service
public class ResourceService implements IResourceService {
    public IResourceRepository resourceRepository;
    private IKingdomRepository kingdomRepository;
    private IBuildingService buildingService;
    private int goldProduction;
    private int foodProduction;
    private int storage;
    private ITroopService troopService;

    public ResourceService(IResourceRepository resourceRepository, IKingdomRepository kingdomRepository, IBuildingService buildingService, ITroopService troopService) {
        this.resourceRepository = resourceRepository;
        this.kingdomRepository = kingdomRepository;
        this.buildingService = buildingService;
        this.troopService = troopService;
    }

    public Iterable<Resource> findAllResources() {
        return resourceRepository.findAll();
    }

    public Resource findByTypeAndKingdom(ResourceType type, Kingdom kingdom) {
        return resourceRepository.findByTypeAndKingdom(type, kingdom);
    }

    public void setStartingResources(Kingdom kingdom) {
        resourceRepository.save(new Resource(ResourceType.GOLD, 100, 1, kingdom));
        resourceRepository.save(new Resource(ResourceType.FOOD, 0, 1, kingdom));
    }

    public void calculateProduction() {
        for (var kingdom : kingdomRepository.findAll()) {
            this.foodProduction = 0;
            this.goldProduction = 0;
            for (var building : kingdom.getBuildings()) {
                switch (building.getType()) {
                    case MINE -> {
                        this.goldProduction += building.getGoldGeneration();
                    }
                    case TOWNHALL -> {
                        this.storage = building.getStorage();
                        this.goldProduction += building.getGoldGeneration();
                        this.foodProduction += building.getFoodGeneration();
                    }
                    case FARM -> {
                        this.foodProduction += building.getFoodGeneration();
                    }
                }
            }
            var resources = kingdom.getResources();
            for (var resource : resources) {
                switch (resource.getType()) {
                    case GOLD -> {
                        resource.setGeneration(goldProduction);
                        resource.generateResources(storage);
                    }
                    case FOOD -> {
                        resource.setGeneration(foodProduction - kingdom.getTroops().size() * TROOP_FOOD_USAGE);
                        if (resource.getAmount() + resource.getGeneration() < 0) {
                            troopService.starveTroops(-(resource.getAmount() + resource.getGeneration()), kingdom);
                            resource.setAmount(0);
                        } else {
                            resource.generateResources(storage);
                        }
                    }
                }
            }
            resourceRepository.saveAll(resources);
        }
    }

    @Override
    public int lootResource(long lootedKingdomId, int maxLootedResources, ResourceType resourceType) {
        Kingdom lootedKingdom = kingdomRepository.findById(lootedKingdomId).get();
        Resource resource = findByTypeAndKingdom(resourceType, lootedKingdom);
        int availableResource = resource.getAmount();
        if(maxLootedResources<availableResource){
            resource.setAmount(availableResource-maxLootedResources);
            resourceRepository.save(resource);
            return maxLootedResources;
        }else{
            resource.setAmount(0);
            resourceRepository.save(resource);
            return availableResource;
        }
    }

    @Override
    public void addResource(long kingdomId, int amount, ResourceType resourceType) {
        Kingdom kingdom = kingdomRepository.findById(kingdomId).get();
        Resource resource = findByTypeAndKingdom(resourceType, kingdom);
        int storage = buildingService.getByTypeAndKingdomId(BuildingType.TOWNHALL, kingdomId).getStorage();
        int newAmount = resource.amount + amount;
        if (newAmount < storage){
            resource.setAmount(newAmount);
        }else{
            resource.setAmount(storage);
        }
        resourceRepository.save(resource);
    }
}