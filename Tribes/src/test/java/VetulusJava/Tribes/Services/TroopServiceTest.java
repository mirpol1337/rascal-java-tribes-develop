package VetulusJava.Tribes.Services;

import VetulusJava.Tribes.Services.BuyService.BuyResponse;
import VetulusJava.Tribes.Services.SeedingService.InitializeDB;
import VetulusJava.Tribes.Entities.Building;
import VetulusJava.Tribes.Entities.Constants;
import VetulusJava.Tribes.Entities.Kingdom;
import VetulusJava.Tribes.Entities.Troop;
import VetulusJava.Tribes.Enum.BuildingType;
import VetulusJava.Tribes.Exceptions.BadRequestException;
import VetulusJava.Tribes.Repositories.IKingdomRepository;
import VetulusJava.Tribes.Repositories.ITroopRepository;
import VetulusJava.Tribes.Services.BuildingService.IBuildingService;
import VetulusJava.Tribes.Services.TroopService.ITroopService;
import VetulusJava.Tribes.Services.TroopService.TroopService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase
@TestPropertySource(
        locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TroopServiceTest {
    @Autowired
    ITroopService troopService;
    @Autowired
    ITroopRepository troopRepository;
    @Autowired
    IKingdomRepository kingdomRepository;
    @MockBean
    IBuildingService buildingService;
    @Autowired
    InitializeDB initializeDB;
    private Kingdom testKingdom;
    private BuyResponse buyResponse;

    @BeforeAll
    private void init() {
        initializeDB.seedDB();
        this.troopService = new TroopService(troopRepository, buildingService);
        this.testKingdom = kingdomRepository.findById(1L).get();
    }

    @BeforeEach
    void cleanTroopDB() {
        troopRepository.deleteAll();
    }

    @Test
    @DisplayName("Get troop by Id")
    public void getByIdTest() {
        troopRepository.deleteAll();
        Troop testTroop = new Troop(3);
        testTroop.setKingdom(testKingdom);
        troopRepository.save(testTroop);
        long testTroopId = testTroop.getId();
        Troop actualTroop = troopService.getById(testTroopId);
        assertEquals(testTroop.getLevel(), actualTroop.getLevel());
        if (troopRepository.findById((long) 5).isPresent()) {
            troopRepository.deleteById((long) 5);
        }
        assertNull(troopService.getById(5));
    }

    @Test
    @DisplayName("create troop success")
    public void createTroopTest() {
        BuyResponse buyResponse = new BuyResponse();
        long currentTime = System.currentTimeMillis() / 1000;
        when(buildingService.getByTypeAndKingdomId(BuildingType.BARRACKS, 1L))
                .thenReturn(new Building(BuildingType.BARRACKS, 5, 10, 1000L, 1111L));
        Troop expectedTroop = new Troop(1, 5, 50, 1, 1, currentTime, currentTime + Constants.TROOP_TRAINING_TIME, testKingdom);
        Troop actualTroop = (Troop) troopService.createOrUpdateTroop(null, testKingdom, buyResponse).getTroop();
        assertEquals(expectedTroop.getLevel(), actualTroop.getLevel());
        assertEquals(expectedTroop.getHp(), actualTroop.getHp());
    }

    @Test
    @DisplayName("create troop, max queue length exceeded")
    public void createTroopTestMaxQueueExceeded() {
        BuyResponse buyResponse = null;
        when(buildingService.getByTypeAndKingdomId(BuildingType.BARRACKS, 1L))
                .thenReturn(new Building(BuildingType.BARRACKS, 3, 10, 1000L, 1111L));
        for (int i = 0; i < 5; i++) { //fill the queue with 5 troops and then try creating next one. It should result in an exception
            buyResponse = new BuyResponse();
            troopService.createOrUpdateTroop(null, testKingdom, buyResponse);
        }
        buyResponse = troopService.createOrUpdateTroop(null, testKingdom, buyResponse);
        assertEquals("Max queue length is 5", buyResponse.getMessage());
    }

    @Test
    @DisplayName("update troop success")
    public void updateTroopTestSuccess() {
        BuyResponse buyResponse = new BuyResponse();
        long currentTime = System.currentTimeMillis() / 1000;
        Troop testTroop = new Troop(4, currentTime, testKingdom);
        troopRepository.save(testTroop);
        Troop updatedTroop = (Troop) troopService.createOrUpdateTroop(5, testKingdom, buyResponse).getTroop();
        assertEquals(testTroop.getLevel() + 1, updatedTroop.getLevel());
        assertEquals(testTroop.getAttack() + 1, updatedTroop.getAttack());
        assertEquals(testTroop.getDefense() + 1, updatedTroop.getDefense());
    }

    @Test
    @DisplayName("update troop no eligible troop found")
    public void updateTroopTestNoEligibleTroopFound() {
        BuyResponse buyResponse = new BuyResponse();
        long currentTime = System.currentTimeMillis() / 1000;
        Troop expectedTroop = new Troop(4, currentTime, testKingdom); //save level 4 troop as only troop in DB and try to update him to level 10
        troopRepository.save(expectedTroop);
        buyResponse = troopService.createOrUpdateTroop(10, testKingdom, buyResponse);
        assertEquals("No troop of level 9 found", buyResponse.getMessage());
    }

    @Test
    @DisplayName("update troop queue length exceeded")
    public void updateTroopTestQueueLengthExceeded() {
        troopRepository.deleteAll();
        BuyResponse buyResponse = new BuyResponse();
        for (int i = 0; i < 6; i++) {   //fill db with 5
            Troop testTroop = new Troop(1, 1000, testKingdom); //create 6 troops in db
            troopRepository.save(testTroop);
        }
        for (int i = 0; i < 5; i++) {  //order upgrade for five troops and then call it for 6th one. It should throw an exception.
            troopService.createOrUpdateTroop(2, testKingdom, buyResponse);
        }
        buyResponse = troopService.createOrUpdateTroop(2, testKingdom, buyResponse);
        assertEquals("Max queue length is 5", buyResponse.getMessage());
    }

    @Test
    @DisplayName("Starve troops - 2 troops too many")
    public void StarveTroopsSuccess() {
        for (int i = 0; i < 5; i++) {
            Troop testTroop = new Troop(i, 1000, testKingdom);  // create 5 troops and let 2 starve out
            troopRepository.save(testTroop);
        }
        troopService.starveTroops(2, testKingdom);
        assertEquals(3, troopService.getTroops(testKingdom.getId()).size()); //check that 3 troops remain
    }

    @Test
    @DisplayName("Starve troops - by the level. The lower level dies first")
    public void StarveTroopsByLevel() {
        troopRepository.deleteAll();
        for (int i = 1; i < 6; i++) {
            Troop testTroop = new Troop(i, 1000, testKingdom);  // create 5 troops with rising level
            troopRepository.save(testTroop);
        }
        troopService.starveTroops(4, testKingdom);
        Troop survivor = troopService.getTroops(testKingdom.getId()).stream().findFirst().get();
        assertEquals(5, survivor.getLevel()); //check that the survivor has the highest level
    }
}