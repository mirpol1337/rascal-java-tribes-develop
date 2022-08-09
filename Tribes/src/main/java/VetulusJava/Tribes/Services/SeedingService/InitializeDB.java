package VetulusJava.Tribes.Services.SeedingService;

import VetulusJava.Tribes.Entities.*;
import VetulusJava.Tribes.Enum.BuildingType;
import VetulusJava.Tribes.Enum.ResourceType;
import VetulusJava.Tribes.Repositories.*;
import org.springframework.stereotype.Component;

@Component
public class InitializeDB {

    private IUserRepository userRepository;
    private IKingdomRepository kingdomRepository;
    private IBuildingRepository buildingRepository;
    private ITroopRepository troopRepository;
    private IResourceRepository resourceRepository;

    private ApplicationUser testUser;
    private Kingdom testKingdom;
    private Building testBarracks;
    private Building testTownHall;
    private Building testMine;
    private Building testFarm;
    private Troop testTroop;
    private Resource testGold;
    private Resource testFood;

    private void createFreshObjects() {
        this.testUser = new ApplicationUser("Bond", "$2y$12$6DoT4V07hpRiTvEQs4S91uunNK5UvEt91BLK86XDv5Nha70p/oEKu", null); //decrypted password is 123
        this.testKingdom = new Kingdom("TestKingdom", testUser);
        this.testBarracks = new Building(BuildingType.BARRACKS, 5, 15, 1000L, 1200L, testKingdom);
        this.testTownHall = new Building(BuildingType.TOWNHALL, 10, 15, 1000L, 1200L, testKingdom);
        this.testMine = new Building(BuildingType.MINE, 5, 15, 1000L, 1200L, testKingdom);
        this.testFarm = new Building(BuildingType.FARM, 5, 15, 1000L, 1200L, testKingdom);
        this.testTroop = new Troop(1, 1200, testKingdom);
        this.testGold = new Resource(ResourceType.GOLD, 100, 1, testKingdom);
        this.testFood = new Resource(ResourceType.FOOD, 100, 1, testKingdom);
        testUser.setKingdom(testKingdom);
    }

    public InitializeDB(IUserRepository userRepository, IKingdomRepository kingdomRepository, IBuildingRepository buildingRepository, ITroopRepository troopRepository, IResourceRepository resourceRepository) {
        this.userRepository = userRepository;
        this.kingdomRepository = kingdomRepository;
        this.buildingRepository = buildingRepository;
        this.troopRepository = troopRepository;
        this.resourceRepository = resourceRepository;
    }

    public void cleanDB() {
        resourceRepository.deleteAll();
        troopRepository.deleteAll();
        buildingRepository.deleteAll();
        kingdomRepository.deleteAll();
        userRepository.deleteAll();
    }

    public void seedDB() {
        cleanDB();
        createFreshObjects();
        seedUser();
        seedKingdom();
        seedResources();
        seedBuildings();
        seedTroop();
    }

    public void seedUser() {
        userRepository.save(testUser);
    }

    public void seedKingdom() {
        kingdomRepository.save(testKingdom);
    }

    public void seedBuildings() {
        buildingRepository.save(testBarracks);
        buildingRepository.save(testTownHall);
        buildingRepository.save(testMine);
        buildingRepository.save(testFarm);
    }

    public void seedTroop() {
        troopRepository.save(testTroop);
    }

    public void seedResources() {
        resourceRepository.save(testGold);
        resourceRepository.save(testFood);
    }
}
