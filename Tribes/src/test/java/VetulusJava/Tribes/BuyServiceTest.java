package VetulusJava.Tribes;

import VetulusJava.Tribes.DTOs.CreateUserDto;
import VetulusJava.Tribes.Entities.Building;
import VetulusJava.Tribes.Entities.Kingdom;
import VetulusJava.Tribes.Entities.Resource;
import VetulusJava.Tribes.Enum.BuildingType;
import VetulusJava.Tribes.Enum.ResourceType;
import VetulusJava.Tribes.Repositories.IBuildingRepository;
import VetulusJava.Tribes.Exceptions.BadRequestException;
import VetulusJava.Tribes.Repositories.IKingdomRepository;
import VetulusJava.Tribes.Repositories.IResourceRepository;
import VetulusJava.Tribes.Services.BuildingService.IBuildingService;
import VetulusJava.Tribes.Services.BuyService.BuyRequest;
import VetulusJava.Tribes.Services.BuyService.BuyService;
import VetulusJava.Tribes.Services.BuyService.BuyingType;
import VetulusJava.Tribes.Services.BuyService.IBuyService;
import VetulusJava.Tribes.Services.BuyService.*;
import VetulusJava.Tribes.Services.SeedingService.InitializeDB;
import VetulusJava.Tribes.Services.TroopService.ITroopService;
import VetulusJava.Tribes.Services.UserService.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class BuyServiceTest {
    @Autowired
    IBuyService buyService;
    @Autowired
    IUserService userService;
    @Autowired
    IKingdomRepository kingdomRepository;
    @Autowired
    IResourceRepository resourceRepository;
    @Autowired
    IBuildingService buildingService;
    @Autowired
    ITroopService troopService;
    @Autowired
    InitializeDB initializeDB;
    @Autowired
    IBuildingRepository buildingRepository;

    @BeforeEach
    void setUp() {
     buyService = new BuyService(resourceRepository, troopService, buildingService, buildingRepository);
    }

    @Test
    public void payForBuyTroop() {
        CreateUserDto createUserDto = new CreateUserDto("Karoína", "karoina@mail.cz", "Karoín", "12345678");
        userService.registerNewUserAccount(createUserDto, createUserDto.getPassword());
        Kingdom kingdom = kingdomRepository.findKingdomByName("Karoín");
        BuyRequest buyRequest = new BuyRequest(kingdom, BuyingType.TROOP);
        buyService.buy(buyRequest);
        Resource actual = resourceRepository.findByTypeAndKingdom(ResourceType.GOLD, kingdom);
        assertThat(actual.getAmount()).isEqualTo(90);
    }

    @Test
    public void payForTraingTroop() {
        CreateUserDto createUserDto = new CreateUserDto("Karolína", "karolina@mail.cz", "Karolín", "12345678");
        userService.registerNewUserAccount(createUserDto, createUserDto.getPassword());
        Kingdom kingdom = kingdomRepository.findKingdomByName("Karolín");
        BuyRequest buyRequest = new BuyRequest(kingdom, BuyingType.TROOP);
        buyService.buy(buyRequest);
        BuyRequest buyRequest2 = new BuyRequest(kingdom, BuyingType.TRAINTROOP, 2);
        buyService.buy(buyRequest2);
        Resource actual = resourceRepository.findByTypeAndKingdom(ResourceType.GOLD, kingdom);
        assertThat(actual.getAmount()).isEqualTo(85);
    }

    @Test
    public void upgradeBuildingWithLowerTownHallLevel() {
        initializeDB.cleanDB();
        initializeDB.seedDB();
        var kingdom = kingdomRepository.findKingdomByName("TestKingdom");
        var goldResource = resourceRepository.findByTypeAndKingdom(ResourceType.GOLD, kingdom);
        var townHall = buildingRepository.findByKingdomAndType(kingdom, BuildingType.TOWNHALL);
        townHall.setLevel(5);
        buildingRepository.save(townHall);
        var buyRequest = new BuyRequest(kingdom, BuyingType.UPGRADEBUILDING, 6, buildingService.getByTypeAndKingdomId(BuildingType.BARRACKS, kingdom.getId()).getID());
        goldResource.setAmount(2000);
        resourceRepository.save(goldResource);
        BuyResponse actual = (BuyResponse) buyService.buy(buyRequest);
        assertEquals(HttpStatus.BAD_REQUEST, actual.getHttpStatus());
        assertEquals("You cannot upgrade to this level, upgrade your Town Hall first!", actual.getMessage());
    }

    @Test
    public void upgradeTownHallSuccess() {
        initializeDB.cleanDB();
        initializeDB.seedDB();
        var kingdom = kingdomRepository.findKingdomByName("TestKingdom");
        var goldResource = resourceRepository.findByTypeAndKingdom(ResourceType.GOLD, kingdom);
        goldResource.setAmount(2000);
        resourceRepository.save(goldResource);
        var buyRequest = new BuyRequest(kingdom, BuyingType.UPGRADEBUILDING, 11, buildingService.getByTypeAndKingdomId(BuildingType.TOWNHALL, kingdom.getId()).getID());
        BuyResponse buyResponse = (BuyResponse) buyService.buy(buyRequest);
        var actual = resourceRepository.findByTypeAndKingdom(ResourceType.GOLD, kingdom);
        assertThat(actual.getAmount()).isEqualTo(900);
        assertThat(buyResponse.getBuilding().level).isEqualTo(11);
    }

    @Test
    public void upgradeTownHallAboveMaxLevel() {
        initializeDB.cleanDB();
        initializeDB.seedDB();
        var kingdom = kingdomRepository.findKingdomByName("TestKingdom");
        var goldResource = resourceRepository.findByTypeAndKingdom(ResourceType.GOLD, kingdom);
        goldResource.setAmount(2100);
        resourceRepository.save(goldResource);
        var townHall = buildingRepository.findByKingdomAndType(kingdom, BuildingType.TOWNHALL);
        townHall.setLevel(20);
        buildingRepository.save(townHall);
        var buyRequest = new BuyRequest(kingdom, BuyingType.UPGRADEBUILDING, 21, buildingService.getByTypeAndKingdomId(BuildingType.TOWNHALL, kingdom.getId()).getID());
        BuyResponse buyResponse = (BuyResponse) buyService.buy(buyRequest);
        assertEquals(HttpStatus.BAD_REQUEST, buyResponse.getHttpStatus());
        assertEquals("You have reached maximum level: 20!", buyResponse.getMessage());
    }

    @Test
    @DisplayName("try downgrade level of building")
    public void upgradeTownHallAboveActualLevel() {
        initializeDB.cleanDB();
        initializeDB.seedDB();
        var kingdom = kingdomRepository.findKingdomByName("TestKingdom");
        var goldResource = resourceRepository.findByTypeAndKingdom(ResourceType.GOLD, kingdom);
        goldResource.setAmount(2100);
        resourceRepository.save(goldResource);
        var townHall = buildingRepository.findByKingdomAndType(kingdom, BuildingType.TOWNHALL);
        townHall.setLevel(10);
        buildingRepository.save(townHall);
        var buyRequest = new BuyRequest(kingdom, BuyingType.UPGRADEBUILDING, 9, buildingService.getByTypeAndKingdomId(BuildingType.TOWNHALL, kingdom.getId()).getID());
        BuyResponse buyResponse = (BuyResponse) buyService.buy(buyRequest);
        assertEquals(HttpStatus.BAD_REQUEST, buyResponse.getHttpStatus());
        assertEquals("You cannot put down your level!", buyResponse.getMessage());
    }

    @Test
    @DisplayName("try upgrade on actual level of building")
    public void upgradeTownHallLevelonActualLevel() {
        initializeDB.cleanDB();
        initializeDB.seedDB();
        var kingdom = kingdomRepository.findKingdomByName("TestKingdom");
        var goldResource = resourceRepository.findByTypeAndKingdom(ResourceType.GOLD, kingdom);
        goldResource.setAmount(2100);
        resourceRepository.save(goldResource);
        var townHall = buildingRepository.findByKingdomAndType(kingdom, BuildingType.TOWNHALL);
        townHall.setLevel(10);
        buildingRepository.save(townHall);
        var buyRequest = new BuyRequest(kingdom, BuyingType.UPGRADEBUILDING, 10, buildingService.getByTypeAndKingdomId(BuildingType.TOWNHALL, kingdom.getId()).getID());
        BuyResponse buyResponse = (BuyResponse) buyService.buy(buyRequest);
        assertEquals(HttpStatus.BAD_REQUEST, buyResponse.getHttpStatus());
        assertEquals("You are already on this level!", buyResponse.getMessage());
    }

    @Test
    @DisplayName("try upgrade on more then plus 1 level of building")
    public void upgradeTownHallLevelonMoreThanOneLevel() {
        initializeDB.cleanDB();
        initializeDB.seedDB();
        var kingdom = kingdomRepository.findKingdomByName("TestKingdom");
        var goldResource = resourceRepository.findByTypeAndKingdom(ResourceType.GOLD, kingdom);
        goldResource.setAmount(2100);
        resourceRepository.save(goldResource);
        var townHall = buildingRepository.findByKingdomAndType(kingdom, BuildingType.TOWNHALL);
        townHall.setLevel(10);
        buildingRepository.save(townHall);
        var buyRequest = new BuyRequest(kingdom, BuyingType.UPGRADEBUILDING, 12, buildingService.getByTypeAndKingdomId(BuildingType.TOWNHALL, kingdom.getId()).getID());
        BuyResponse buyResponse = (BuyResponse) buyService.buy(buyRequest);
        assertEquals(HttpStatus.BAD_REQUEST, buyResponse.getHttpStatus());
        assertEquals("You cannot upgrade your building more than +1 level", buyResponse.getMessage());
    }
}
